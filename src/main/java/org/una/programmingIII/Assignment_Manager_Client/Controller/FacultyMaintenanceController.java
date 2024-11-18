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
import org.una.programmingIII.Assignment_Manager_Client.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.FacultyService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

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
    private ImageView imvSearch;


    private FacultyService facultyService;
    private UniversityService universityService;
    private UniversityDto universityDto;
    private FacultyDto facultyDto;
    private RequiredFieldsValidator validator;
    private final UserDto userSession = SessionManager.getInstance().getLoginResponse().getUser();


    @Override
    public void initialize() {
        initializeServices();
        initializeDtos();
        updateUniversityInputLabel();
        setupTableColumns();
        setupValidator();
        loadUniversityFaculties();
        manageUserPermissionsAndButtons();
    }
    private void manageUserPermissionsAndButtons() {
        btnSave.setDisable(
                !(userSession.getPermissions().stream().anyMatch(permission -> permission.getName().equals(PermissionType.CREATE_FACULTIES)) ||
                        userSession.getPermissions().stream().anyMatch(permission -> permission.getName().equals(PermissionType.EDIT_FACULTIES))));
    }

    private void initializeServices() {
        facultyService = new FacultyService();
        universityService = new UniversityService();
    }

    private void initializeDtos() {
        universityDto = new UniversityDto();
        facultyDto = new FacultyDto();
    }

    private void setupTableColumns() {
        clmnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmDelete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        clmDelete.setCellFactory(p -> new ButtonCellDelete());
        clmDepartment.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        clmDepartment.setCellFactory(p -> new ButtonCellDepartment());
        tbvFaculty.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void setupValidator() {
        validator = new RequiredFieldsValidator(Arrays.asList(txfFacultyName));
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
        FlowController.getInstance().deleteAndLoadView("UniversityMaintenanceView");
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {
    }

    private void loadUniversityFaculties() {
        if (userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.VIEW_FACULTIES))){
            new Message().showModal(Alert.AlertType.WARNING, "Permission Error", getStage(), "You do not have permission to view faculties.");
            return;
        }
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
        if (userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.CREATE_FACULTIES))){
            new Message().showModal(Alert.AlertType.WARNING, "Permission Error", getStage(), "You do not have permission to create faculties.");
            return;
        }
        String invalids = validator.validate();
        if (!(invalids.isBlank())) {
            new Message().showModal(Alert.AlertType.ERROR, "Create Faculty", getStage(), invalids);
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
        if (userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.EDIT_FACULTIES))){
            new Message().showModal(Alert.AlertType.WARNING, "Permission Error", getStage(), "You do not have permission to edit faculties.");
            return;
        }
        String invalids = validator.validate();
        if (!(invalids.isBlank())) {
            new Message().showModal(Alert.AlertType.ERROR, "Update Faculty", getStage(), invalids);
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

    private class ButtonCellDepartment extends ButtonCellBase<FacultyDto> {
        ButtonCellDepartment() {
            super("Department", "mfx-btn-Enter");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            facultyDto = getTableView().getItems().get(getIndex());
            AppContext.getInstance().set("facultyDto", facultyDto);
            FlowController.getInstance().deleteAndLoadView("DepartmentMaintenanceView");
        }
    }

    private class ButtonCellDelete extends ButtonCellBase<FacultyDto> {
        ButtonCellDelete() {
            super("Delete", "mfx-btn-Delete");
            setDisable(userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.DELETE_FACULTIES)));
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