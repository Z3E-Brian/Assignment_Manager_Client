package org.una.programmingIII.Assignment_Manager_Client.Controller;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.FacultyService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;

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
    private TableColumn<FacultyDto, Long> clmnId;

    @FXML
    private TableColumn<FacultyDto, String> clmnName;

    @FXML
    private Label lblUniversity;

    @FXML
    private TableView<FacultyDto> tbvFaculty;

    @FXML
    private MFXTextField txfFacultyName;

    @FXML
    private MFXTextField txfId;

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
    private List<Node> requeridos;


    @Override

    public void initialize() {
        requeridos = new ArrayList<>();
        facultyService = new FacultyService();
        universityService = new UniversityService();
        universityDto = new UniversityDto();
        facultyDto = new FacultyDto();
        updateUniversityInputLabel();

        clmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbvFaculty.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        indicateRequeridos();
        txfId.setDisable(true);
        loadUniversityFaculties();

    }


    @FXML
    void onActionBtnDepartments(ActionEvent event) {
        if (tbvFaculty.getSelectionModel().getSelectedItem() != null && facultyDto != null) {
            AppContext.getInstance().set("facultyDto", facultyDto);
            FlowController.getInstance().goViewInWindow("DepartmentMaintenanceView");
            ((Stage) btnDepartments.getScene().getWindow()).close();
        }
    }

    @FXML
    void onActionBtnAddFaculty(ActionEvent event) throws Exception {
        clean();
    }


    @FXML
    void onActionBtndeleteFaculty(ActionEvent event) throws Exception {
        if (!(txfId.getText().isEmpty())) {
            facultyService.deleteFaculty(facultyDto.getId());
            clean();
            loadUniversityFaculties();
        } else {
            new Message().showModal(Alert.AlertType.WARNING, "Eliminar facultad", getStage(), "Debe selecionar una de las facultades en la tabla para poder eliminarla.");
        }
    }


    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (!(txfId.getText().isBlank())) {
            updateFaculty();
        } else {
            createFaculty();
        }
    }

    @FXML
    void OnMousePressedTbvFaculty(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
            facultyDto = tbvFaculty.getSelectionModel().getSelectedItem();
            bindear();
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
            new Message().showModal(Alert.AlertType.WARNING, "Error de conexion", getStage(), "Debe selecionar una de las universidades en la tabla para poder eliminarla.");

        }
    }

    private void bindear() {
        txfId.setText(facultyDto.getId().toString());
        txfFacultyName.setText(facultyDto.getName());
    }

    private void updateUniversityInputLabel() {
        universityDto = (UniversityDto) AppContext.getInstance().get("universityDto");
        lblUniversity.setText(universityDto.getName());
    }

    private void createFaculty() throws Exception {
        String invalids = validarRequeridos();
        if (!(invalids.isBlank())) {
            System.out.println(invalids);
            new Message().showModal(Alert.AlertType.ERROR, "Crear facultad", getStage(), "Existen espacios  importantes en blanco");
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
        String invalids = validarRequeridos();
        if (!(invalids.isBlank())) {
            System.out.println(invalids);
            new Message().showModal(Alert.AlertType.ERROR, "Actualizar facultad", getStage(), "Existen espacios  importantes en blanco");
        } else {
            facultyDto.setName(txfFacultyName.getText());
            facultyDto = facultyService.updateFaculty(facultyDto.getId(), facultyDto);
            loadUniversityFaculties();
        }
        clean();
    }

    private void clean() {
        this.txfFacultyName.clear();
        this.txfId.clear();
        facultyDto = new FacultyDto();
        tbvFaculty.getSelectionModel().clearSelection();
    }

    private void indicateRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txfFacultyName));
    }


    public String validarRequeridos() {
        boolean valids = true;
        String invalidSpaces = "";
        for (Node node : requeridos) {
            if (node instanceof MFXTextField && (((MFXTextField) node).getText() == null || ((MFXTextField) node).getText().isBlank())) {
                if (valids) {
                    invalidSpaces += ((MFXTextField) node).getFloatingText();
                } else {
                    invalidSpaces += "," + ((MFXTextField) node).getFloatingText();
                }
                valids = false;
            }
        }
        if (valids) {
            return "";
        } else {
            return "Campos requeridos o con problemas de formato [" + invalidSpaces + "].";
        }
    }

}