package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.DepartmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.FacultyService;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;

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
    private TableColumn<DepartmentDto, Long> clmnId;

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
    private MFXTextField txfId;

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

        clmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmDepartment.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbvDepartment.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        txfId.setDisable(true);
        loadDepartments();
    }

    @FXML
    void OnMousePressedTbvDepartment(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1 && tbvDepartment.getSelectionModel().getSelectedItem() != null) {
            departmentDto = tbvDepartment.getSelectionModel().getSelectedItem();
            bind();
        }
    }

    @FXML
    void onActionBtnDelete(ActionEvent event) throws Exception {
        if (!(txfId.getText().isEmpty())) {
            departmentService.deleteDepartment(departmentDto.getId());
            clean();
            loadDepartments();
        } else {
            new Message().showModal(Alert.AlertType.WARNING, "Eliminar departamento", getStage(), "Debe selecionar uno de los departamentos en la tabla para poder eliminarlo.");
        }
    }

    @FXML
    void onActionBtnNewDepartment(ActionEvent event) {
        clean();
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (!(txfId.getText().isBlank())) {
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
            new Message().showModal(Alert.AlertType.ERROR, "Crear departamento", getStage(), "Existen espacios  importantes en blanco");
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
            new Message().showModal(Alert.AlertType.ERROR, "Actualizar Departamento", getStage(), "Existen espacios  importantes en blanco");
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
            new Message().showModal(Alert.AlertType.WARNING, "Error de conexion", getStage(), "Debe selecionar una de las universidades en la tabla para poder eliminarla.");

        }
    }

    private void clean() {
        this.txfDepartmentName.clear();
        this.txfId.clear();
        departmentDto = new DepartmentDto();
    }

    private void bind() {
        txfId.setText(departmentDto.getId().toString());
        txfDepartmentName.setText(departmentDto.getName());
    }

}
