package org.una.programmingIII.Assignment_Manager_Client.Controller;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.FacultyService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FacultyMaintenanceController extends Controller {

    @FXML
    private MFXButton btnAddFaculty;

    @FXML
    private MFXButton btnDeleteFaculty;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXButton btnDepartments;


    @FXML
    private TableColumn<FacultyDto, String> clmnName;
    @FXML
    private TableColumn<FacultyDto, Boolean> clmDelete;
    @FXML
    private TableColumn<FacultyDto, Boolean> clmDepartment;

    @FXML
    private Label lblUniversity;

    @FXML
    private TableView<FacultyDto> tbvFaculty;

    @FXML
    private MFXTextField txfFacultyName;


    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvClose;

    @FXML
    private ImageView imvSearch;


    private FacultyService facultyService;
    private UniversityService universityService;
    private UniversityDto universityDto;
    private FacultyDto facultyDto;
    private List<Node> required;


    @Override

    public void initialize() {
        required = new ArrayList<>();
        facultyService = new FacultyService();
        universityService = new UniversityService();
        universityDto = new UniversityDto();
        facultyDto = new FacultyDto();
        updateUniversityInputLabel();

        clmnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmDelete.setCellValueFactory((TableColumn.CellDataFeatures<FacultyDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        clmDelete.setCellFactory((TableColumn<FacultyDto, Boolean> p) -> new ButtonCellDelete());
        clmDepartment.setCellValueFactory((TableColumn.CellDataFeatures<FacultyDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        clmDepartment.setCellFactory((TableColumn<FacultyDto, Boolean> p) -> new ButtonCellDepartment());
        tbvFaculty.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        indicateRequired();
        loadUniversityFaculties();

    }


    @FXML
    void onActionBtnAddFaculty(ActionEvent event) throws Exception {
        clean();
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (!(facultyDto.getId() == null)) {
            updateFaculty();
        } else {
            createFaculty();
        }
    }

    @FXML
    void OnMousePressedTbvFaculty(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1 && tbvFaculty.getSelectionModel().getSelectedItem() != null) {
            facultyDto = tbvFaculty.getSelectionModel().getSelectedItem();
            bind();
        }
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().goViewInWindow("UniversityMaintenanceView");
        ((Stage) btnDepartments.getScene().getWindow()).close();
    }

    @FXML
    void onMouseClickedImvClose(MouseEvent event) {
        System.out.println("imvClose");
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {
        System.out.println("imvSearch");
    }

    private void loadUniversityFaculties() {
        try {
            universityDto = universityService.getUniversityById(universityDto.getId());
            List<FacultyDto> facultyDtoList = universityDto.getFaculties();
            ObservableList<FacultyDto> facultyDtoObservableList = FXCollections.observableArrayList(facultyDtoList);
            tbvFaculty.getItems().clear();
            tbvFaculty.setItems(facultyDtoObservableList);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.WARNING, "Connection Error", getStage(), "You must select one of the universities in the table to delete it.");
        }
    }

    private void bind() {
        txfFacultyName.setText(facultyDto.getName());
    }

    private void updateUniversityInputLabel() {
        universityDto = (UniversityDto) AppContext.getInstance().get("universityDto");
        lblUniversity.setText(universityDto.getName());
    }

    private void createFaculty() throws Exception {
        String invalids = validateRequired();
        if (!(invalids.isBlank())) {
            System.out.println(invalids);
            new Message().showModal(Alert.AlertType.ERROR, "Create Faculty", getStage(), "There are important blank spaces");
        } else {
            facultyDto = new FacultyDto();
            facultyDto.setName(txfFacultyName.getText());
            facultyDto.setUniversityId(universityDto.getId());
            facultyDto = facultyService.createFaculty(facultyDto);
            loadUniversityFaculties();
            clean();
        }
    }

    private void updateFaculty() throws Exception {
        String invalids = validateRequired();
        if (!(invalids.isBlank())) {
            System.out.println(invalids);
            new Message().showModal(Alert.AlertType.ERROR, "Update Faculty", getStage(), "There are important blank spaces");
        } else {
            facultyDto.setName(txfFacultyName.getText());
            facultyDto = facultyService.updateFaculty(facultyDto.getId(), facultyDto);
            loadUniversityFaculties();
        }
        clean();
    }

    private void clean() {
        this.txfFacultyName.clear();
        this.txfFacultyName.requestFocus();
        facultyDto = new FacultyDto();
        tbvFaculty.getSelectionModel().clearSelection();
    }

    private void indicateRequired() {
        required.clear();
        required.addAll(Arrays.asList(txfFacultyName));
    }


    private String validateRequired() {
        StringBuilder invalid = new StringBuilder();
        for (Node node : required) {
            String floatingText = getFloatingText(node);
            if (floatingText != null) {
                if (!invalid.isEmpty()) {
                    invalid.append(", ");
                }
                invalid.append(floatingText);
            }
        }
        return invalid.isEmpty() ? "" : "Fields required or with formatting problems [" + invalid + "].";
    }

    private String getFloatingText(Node node) {
        String floatingText = null;
        if (node instanceof MFXTextField && isEmpty(((MFXTextField) node).getText())) {
            floatingText = ((MFXTextField) node).getFloatingText();
        } else if (node instanceof MFXComboBox && ((MFXComboBox<?>) node).getValue() == null) {
            floatingText = ((MFXComboBox<?>) node).getFloatingText();
        } else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
            floatingText = ((MFXDatePicker) node).getFloatingText();
        }
        return floatingText;
    }

    private boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

private class ButtonCellDepartment extends ButtonCellBase<FacultyDto> {
    ButtonCellDepartment() {
        super("Department", "mfx-btn-Enter");
    }

    @Override
    protected void handleAction(ActionEvent event) {
        facultyDto = getTableView().getItems().get(getIndex());
        AppContext.getInstance().set("facultyDto", facultyDto);
        FlowController.getInstance().goViewInWindow("DepartmentMaintenanceView");
        getStage().close();
    }
}

    private class ButtonCellDelete extends ButtonCellBase<FacultyDto> {
    ButtonCellDelete() {
        super("Delete", "mfx-btn-Delete");
    }

    @Override
    protected void handleAction(ActionEvent event) {
        FacultyDto facultyDtoDelete = getTableView().getItems().get(getIndex());
        try {
            facultyService.deleteFaculty(facultyDtoDelete.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clean();
        loadUniversityFaculties();
    }
}
}