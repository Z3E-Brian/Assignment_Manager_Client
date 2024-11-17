package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Interfaces.SessionObserver;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javafx.scene.input.MouseEvent;

public class UniversityMaintenanceController extends Controller implements SessionObserver {


    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;


    @FXML
    private TableColumn<UniversityDto, String> tbcLocation;

    @FXML
    private TableColumn<UniversityDto, String> tbcName;
    @FXML
    private TableColumn<UniversityDto, Boolean> tbcDelete;

    @FXML
    private TableColumn<UniversityDto, Boolean> tbcFaculty;
    @FXML
    private MFXTextField txfLocation;

    @FXML
    private MFXTextField txfName;

    @FXML
    private TableView<UniversityDto> universityTable;

    @FXML
    private ImageView imvSearch;

    @FXML
    private ImageView back;

    private UniversityService universityService;
    private UniversityDto universityDto;
    private Set<PermissionDto> permissionsDto;

    private RequiredFieldsValidator validator;

    @Override
    public void initialize() {
        universityService = new UniversityService();
        universityDto = new UniversityDto();
        setupTableColumns();
        validator = new RequiredFieldsValidator(Arrays.asList(txfName, txfLocation));

        loadUniversities();
        SessionManager.getInstance().addObserver(this);
        SessionManager.getInstance().startTokenValidationTask();
    }

    private void setupTableColumns() {
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tbcDelete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tbcDelete.setCellFactory(p -> new ButtonCellDelete());
        tbcFaculty.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tbcFaculty.setCellFactory(p -> new ButtonCellFaculty());
        universityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    @FXML
    void onActionBtnNew(ActionEvent event) throws Exception {
        clean();
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        try {
            if (!(universityDto.getId() == null)) {
                updateUniversity();
            } else {
                createUniversity();
            }
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, e.getMessage(), getStage(), "An error occurred while saving the university");
        }
    }


    @FXML
    void onMousePressedUniversityTable(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1 && universityTable.getSelectionModel().getSelectedItem() != null) {
            universityDto = universityTable.getSelectionModel().getSelectedItem();
            bind();
        }
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().delete("UniversityMaintenanceView");
        FlowController.getInstance().goMain();
    }

    private void clean() {
        this.txfLocation.clear();
        this.txfName.clear();
        this.txfName.requestFocus();
        universityDto = new UniversityDto();
        universityTable.getSelectionModel().clearSelection();
        SessionManager.getInstance().setRunningTokenValidationThread(true);
    }

    private void createUniversity() throws Exception {
        if (permissionsDto.stream().anyMatch(permission -> permission.getName() == PermissionType.CREATE_UNIVERSITIES)){
            new Message().showModal(Alert.AlertType.ERROR, "Create University", getStage(), "You don't have permission to create universities");
            return;
        }
        String invalids = validator.validate();

        if (!(invalids.isBlank())) {
            new Message().showModal(Alert.AlertType.ERROR, "Create University", getStage(), invalids);
        } else {
            universityDto = new UniversityDto();
            universityDto.setName(txfName.getText());
            universityDto.setLocation(txfLocation.getText());
            universityService.createUniversity(universityDto);
            clean();
            loadUniversities();
        }
    }

    private void updateUniversity() throws Exception {
        if (permissionsDto.stream().anyMatch(permission -> permission.getName() == PermissionType.EDIT_UNIVERSITIES)){
            new Message().showModal(Alert.AlertType.ERROR, "Update University", getStage(), "You don't have permission to edit universities");
            return;
        }

        universityDto.setLocation(txfLocation.getText());
        universityDto.setName(txfName.getText());
        universityDto = universityService.updateUniversity(universityDto.getId(), universityDto);
        loadUniversities();
        clean();
    }

    private void loadUniversities() {
        if (permissionsDto.stream().noneMatch(permission -> permission.getName() == PermissionType.VIEW_UNIVERSITIES)) {
            new Message().showModal(Alert.AlertType.ERROR, "Access to Data Denied", getStage(), "You don't have permission to view universities");
            return;
        }
        try {
            List<UniversityDto> universityDtoList = universityService.getAllUniversities();
            ObservableList<UniversityDto> universityDtoObservableList = FXCollections.observableArrayList(universityDtoList);
            universityTable.getItems().clear();
            universityTable.setItems(universityDtoObservableList);
        } catch (java.net.ConnectException e) {
            new Message().showModal(Alert.AlertType.ERROR, "Connection Error", getStage(), "Failed to connect to the server. Please make sure the server is running.");
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, " Error loading Universities", getStage(), "Failed to load Universities.");

        }
    }

    private void bind() {
        txfLocation.setText(universityDto.getLocation());
        txfName.setText(universityDto.getName());
    }

    private void setUniversityDto() {
        AppContext.getInstance().set("universityDto", universityDto);
    }

    private void validateUserFunctions() {
        permissionsDto = SessionManager.getInstance().getLoginResponse().getUser().getPermissions();
        btnSave.setDisable(!
                (permissionsDto.stream().noneMatch(permission -> permission.getName() == PermissionType.CREATE_UNIVERSITIES)||
                permissionsDto.stream().noneMatch(permission -> permission.getName() == PermissionType.EDIT_UNIVERSITIES)));
    }


    @Override
    public void onSessionExpired() {
        new Message().showModal(Alert.AlertType.INFORMATION, "Session timeout", getStage(), "You need to log in again");
        Platform.runLater(() -> {
            FlowController.getInstance().goViewInWindow("LogInView");
            FlowController.getInstance().delete("UniversityMaintenanceView");
            SessionManager.getInstance().removeObserver(this);
            getStage().close();
        });
    }

    private class ButtonCellDelete extends ButtonCellBase<UniversityDto> {
        ButtonCellDelete() {
            super("Delete", "mfx-btn-Delete");
            setDisable(permissionsDto.stream().noneMatch(permission -> permission.getName() == PermissionType.DELETE_UNIVERSITIES));
        }

        @Override
        protected void handleAction(ActionEvent event) {
            try {
                universityDto = getTableView().getItems().get(getIndex());
                universityService.deleteUniversity(universityDto.getId());
                clean();
                loadUniversities();
            } catch (Exception e) {
                new Message().showModal(Alert.AlertType.ERROR, "Error deleting", getStage(), "Could not delete the university");
            }
        }
    }

    private class ButtonCellFaculty extends ButtonCellBase<UniversityDto> {
        ButtonCellFaculty() {
            super("Faculty", "mfx-btn-Enter");
            setDisable(permissionsDto.stream().noneMatch(permission -> permission.getName() == PermissionType.VIEW_FACULTIES));
        }

        @Override
        protected void handleAction(ActionEvent event) {
            universityDto = getTableView().getItems().get(getIndex());
            setUniversityDto();
            FlowController.getInstance().deleteAndLoadView("FacultyMaintenanceView");
        }
    }

}