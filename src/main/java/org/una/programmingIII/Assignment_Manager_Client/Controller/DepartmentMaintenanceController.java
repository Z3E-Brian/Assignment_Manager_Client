package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.DepartmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.FacultyService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.List;

public class DepartmentMaintenanceController extends Controller {

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNewDepartment;

    @FXML
    private MFXButton btnSave;

    @FXML
    private TableColumn<DepartmentDto, String> clmDepartment;

    @FXML
    private TableColumn<DepartmentDto,Boolean> tbcCareer;

    @FXML
    private TableColumn<DepartmentDto, Boolean> tbcDelete;

    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvClose;

    @FXML
    private ImageView imvSearch;

    @FXML
    private Label lblFaculty;

    @FXML
    private TableView<DepartmentDto> tbvDepartment;

    @FXML
    private MFXTextField txfDepartmentName;

    @FXML
    private MFXButton btnCareers;


    private FacultyService facultyService;
    private DepartmentService departmentService;
    private FacultyDto facultyDto;
    private DepartmentDto departmentDto;


    @Override
    public void initialize() {
        facultyService = new FacultyService();
        departmentService = new DepartmentService();
        facultyDto = new FacultyDto();
        departmentDto = new DepartmentDto();
        updateUniversityInputLabel();

        clmDepartment.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcDelete.setCellValueFactory((TableColumn.CellDataFeatures<DepartmentDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcDelete.setCellFactory((TableColumn<DepartmentDto, Boolean> p) -> new ButtonCellDelete());
        tbcCareer.setCellValueFactory((TableColumn.CellDataFeatures<DepartmentDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcCareer.setCellFactory((TableColumn<DepartmentDto, Boolean> p) -> new ButtonCellCareer());
        tbvDepartment.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        loadDepartments();
    }

    @FXML
    void onActionBtnCareers(ActionEvent event) {
        if (tbvDepartment.getSelectionModel().getSelectedItem() != null && facultyDto != null) {
            AppContext.getInstance().set("departmentDto", departmentDto);
            FlowController.getInstance().goViewInWindow("CareerMaintenanceView");
            ((Stage) btnCareers.getScene().getWindow()).close();
        }
    }


    @FXML
    void OnMousePressedTbvDepartment(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1 && tbvDepartment.getSelectionModel().getSelectedItem() != null) {
            departmentDto = tbvDepartment.getSelectionModel().getSelectedItem();
            bind();
        }
    }


 private void deleteDepartment(DepartmentDto departmentDto) {
        try {
            departmentService.deleteDepartment(departmentDto.getId());
            loadDepartments();
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Eliminate department", getStage(), "Department could not be deleted.");
        }
    }
    @FXML
    void onActionBtnNewDepartment(ActionEvent event) {
        clean();
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (!(departmentDto.getId() == null)) {
            updateDepartment();
        } else {
            createDepartment();
        }
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().goViewInWindow("FacultyMaintenanceView");
        ((Stage) btnSave.getScene().getWindow()).close();
    }

    @FXML
    void onMouseClickedImvClose(MouseEvent event) {
        System.out.println("imvClose");
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {
        System.out.println("imvSearch");
    }

    private void createDepartment() throws Exception {
        if ((txfDepartmentName.getText().isBlank())) {
            new Message().showModal(Alert.AlertType.ERROR, "Create Department", getStage(), "There are important blank spaces");
        } else {
            departmentDto = new DepartmentDto();
            departmentDto.setName(txfDepartmentName.getText());
            departmentDto.setFacultyId(facultyDto.getId());
            departmentDto = departmentService.createDepartment(departmentDto);
            clean();
            loadDepartments();
        }
    }

    private void updateDepartment() throws Exception {
        if ((txfDepartmentName.getText().isBlank())) {
            new Message().showModal(Alert.AlertType.ERROR, "Update Department", getStage(), "There are important blank spaces");
        } else {
            departmentDto.setName(txfDepartmentName.getText());
            departmentDto = departmentService.updateDepartment(departmentDto.getId(), departmentDto);
            clean();
            loadDepartments();
        }
    }

    private void updateUniversityInputLabel() {
        facultyDto = (FacultyDto) AppContext.getInstance().get("facultyDto");
        lblFaculty.setText(facultyDto.getName());
    }

    private void loadDepartments() {
        try {
            facultyDto = facultyService.getFacultyById(facultyDto.getId());
            List<DepartmentDto> departmentDtoList = facultyDto.getDepartments();
            ObservableList<DepartmentDto> departmentDtoObservableList = FXCollections.observableArrayList(departmentDtoList);
            tbvDepartment.getItems().clear();
            tbvDepartment.setItems(departmentDtoObservableList);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.WARNING, "Connection Error", getStage(), "You must select one of the universities in the table to delete it.");

        }
    }

    private void clean() {
        this.txfDepartmentName.clear();
        departmentDto = new DepartmentDto();
    }

    private void bind() {
        txfDepartmentName.setText(departmentDto.getName());
    }

    private class ButtonCellDelete extends ButtonCellBase<DepartmentDto> {
        ButtonCellDelete() {
            super("Delete", "mfx-btn-Delete");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            departmentDto = getTableView().getItems().get(getIndex());
            deleteDepartment(departmentDto);
            loadDepartments();
        }
    }
    private class ButtonCellCareer extends ButtonCellBase<DepartmentDto> {
        ButtonCellCareer() {
            super("Career", "mfx-btn-Enter");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            departmentDto = (DepartmentDto)  ButtonCellCareer.this.getTableView().getItems().get(ButtonCellCareer.this.getIndex());
            AppContext.getInstance().set("departmentDto", departmentDto);
            FlowController.getInstance().goViewInWindow("CareerMaintenanceView");
            getStage().close();
        }
    }

}
