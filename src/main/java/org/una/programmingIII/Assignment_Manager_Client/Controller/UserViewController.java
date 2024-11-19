package org.una.programmingIII.Assignment_Manager_Client.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.NewUserDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class UserViewController extends Controller implements Initializable {

    @FXML
    private MFXButton btnNew, btnSave, btnClear;
    @FXML
    private MFXTextField txfNumberID, txfName, txfLastName, txfSecondLastName, txfEmail, txfPassword, txfCareerId;
    @FXML
    private FlowPane fpPermissions;
    @FXML
    private TableView<UserDto> tbvUser;
    @FXML
    private TableColumn<UserDto, String> nameColumn, lastNameColumn, secondLastNameColumn, emailColumn, numberIDColumn;
    @FXML
    private TableColumn<UserDto, MFXButton> clmAction;
    @FXML
    private Pagination pagination;

    private final UserService userService = new UserService();
    private UserInput userInput = new UserInput();
    private List<PermissionDto> permissions;
    private static final int PAGE_SIZE = 25;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final UserDto userSession = SessionManager.getInstance().getLoginResponse().getUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    @Override
    public void initialize() {
        initializePermissions();
        getServerPermissions();
        manageUserPermissionsAndButtons();
        configureTable();
        configureTextFields();
        bindUser();
        configurePagination();
    }

    private void manageUserPermissionsAndButtons() {
        btnSave.setDisable(
                !(userSession.getPermissions().stream().anyMatch(permission -> permission.getName().equals(PermissionType.CREATE_USERS)) ||
                        userSession.getPermissions().stream().anyMatch(permission -> permission.getName().equals(PermissionType.EDIT_USERS)))
        );
    }

    private void configureTable() {
        numberIDColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        secondLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondLastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbvUser.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        clmAction.setCellFactory(col -> new TableCell<>() {
            private final MFXButton detailsButton = new MFXButton("Delete");

            @Override
            protected void updateItem(MFXButton item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                    detailsButton.setOnAction(event -> deleteUser(getTableView().getItems().get(getIndex())));
                    setDisable(userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.DELETE_USERS)));
                }
            }
        });

    }

    private void configureTextFields() {
        txfCareerId.delegateSetTextFormatter(Format.getInstance().integerFormat());
        txfName.delegateSetTextFormatter(Format.getInstance().maxLengthFormat(50));
        txfLastName.delegateSetTextFormatter(Format.getInstance().maxLengthFormat(50));
        txfSecondLastName.delegateSetTextFormatter(Format.getInstance().maxLengthFormat(50));
        txfEmail.delegateSetTextFormatter(Format.getInstance().maxLengthFormat(100));
        txfNumberID.delegateSetTextFormatter(Format.getInstance().maxLengthFormat(12));
    }

    private void initializePermissions() {
        if (!fpPermissions.getChildren().isEmpty()) return;
        for (PermissionType permission : PermissionType.values()) {
            MFXCheckbox checkBox = new MFXCheckbox(permission.toString().replace("_", " "));
            checkBox.setDisable(userSession.getPermissions().stream().noneMatch(userPermission -> userPermission.getName().equals(PermissionType.MANAGE_PERMISSIONS)));
            checkBox.setPrefWidth(180);
            fpPermissions.getChildren().add(checkBox);
        }
    }

    private void bindUser() {
        unbindUser();
        txfName.textProperty().bindBidirectional(userInput.name);
        txfEmail.textProperty().bindBidirectional(userInput.email);
        txfNumberID.textProperty().bindBidirectional(userInput.identificationNumber);
        txfLastName.textProperty().bindBidirectional(userInput.lastName);
        txfSecondLastName.textProperty().bindBidirectional(userInput.secondLastName);
        txfPassword.textProperty().bindBidirectional(userInput.password);
        txfCareerId.textProperty().bindBidirectional(userInput.careerId);
        bindUserPermissions();
    }

    private void bindUserPermissions() {
        if (txfNumberID.isDisabled()) {
            clearPermissions();
            for (Node node : fpPermissions.getChildren()) {
                if (node instanceof MFXCheckbox checkBox) {
                    PermissionType permissionType = PermissionType.valueOf(checkBox.getText().replace(" ", "_"));
                    boolean isSelected = userInput.getPermissions().stream()
                            .anyMatch(permission -> permission.getName().equals(permissionType));
                    checkBox.setSelected(isSelected);
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        PermissionDto permissionDto = permissions.stream()
                                .filter(permission -> permission.getName().equals(permissionType))
                                .findFirst().orElse(null);
                        if (permissionDto != null) {
                            if (newValue) {
                                userInput.getPermissions().add(permissionDto);
                            } else {
                                userInput.getPermissions().remove(permissionDto);
                            }
                        }
                    });
                }
            }
        } else {
            for (Node node : fpPermissions.getChildren()) {
                if (node instanceof MFXCheckbox checkBox) {
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        PermissionType permissionType = PermissionType.valueOf(checkBox.getText().replace(" ", "_"));
                        PermissionDto permissionDto = permissions.stream()
                                .filter(permission -> permission.getName().equals(permissionType))
                                .findFirst().orElse(null);
                        if (permissionDto != null) {
                            if (newValue) userInput.getPermissions().add(permissionDto);
                            else userInput.getPermissions().remove(permissionDto);
                        }
                    });
                }
            }
        }
    }

    private void unbindUser() {
        txfName.textProperty().unbindBidirectional(userInput.name);
        txfEmail.textProperty().unbindBidirectional(userInput.email);
        txfNumberID.textProperty().unbindBidirectional(userInput.identificationNumber);
        txfLastName.textProperty().unbindBidirectional(userInput.lastName);
        txfSecondLastName.textProperty().unbindBidirectional(userInput.secondLastName);
        txfPassword.textProperty().unbindBidirectional(userInput.password);
        txfCareerId.textProperty().unbindBidirectional(userInput.careerId);
        clearPermissions();
    }

    private void newUser() {
           userInput = new UserInput();
            bindUser();
            clearForm();
            setFieldsEditable(true);
            txfName.requestFocus();
    }

    private void clearForm() {
        txfName.setText("");
        txfEmail.setText("");
        txfNumberID.setText("");
        txfLastName.setText("");
        txfSecondLastName.setText("");
        txfPassword.setText("");
        txfCareerId.setText("");
    }


    private void clearPermissions() {
        for (Node node : fpPermissions.getChildren()) {
            if (node instanceof MFXCheckbox checkBox) checkBox.setSelected(false);
        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (txfNumberID.isDisabled()) {
            if (userSession.getPermissions().stream().anyMatch(permission -> permission.getName().equals(PermissionType.EDIT_USERS))) {
                updateUser();
                return;
            }
            showError("Update User", "You don't have permission to update users.");
            return;
        }

        if (userSession.getPermissions().stream().anyMatch(permission -> permission.getName().equals(PermissionType.CREATE_USERS))) {
            createUser();
            return;
        }
        showError("Create User", "You don't have permission to create users.");
    }

    private void createUser() throws Exception {

        NewUserDto newUserDto = new NewUserDto(userInput);
        newUserDto.setPermissions(getPermissions());
        Answer answer = userService.createUser(newUserDto);
        if (!answer.getState()) showError("Create User", answer.getMessage());
        else {
            showInfo("Create User", "User created successfully.");
            newUser();
            refreshTable();
        }
    }

    private void updateUser() throws Exception {
        UserDto selectedUser = tbvUser.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userInput.setId(selectedUser.getId());
            NewUserDto newUserDto = new NewUserDto(userInput);
            newUserDto.setPermissions(getPermissions());
            Answer answer = userService.updateUser(selectedUser.getId(), newUserDto);
            if (!answer.getState()) showError("Update User", answer.getMessage());
            else {
                showInfo("Update User", "User updated successfully.");
                newUser();
                refreshTable();
            }
        } else showError("Update User", "Please select a user to update.");
    }

    private void loadUsers() {
        if (userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.VIEW_USERS)))
            return;
        try {
            List<UserDto> users = userService.getAllUsers();
            tbvUser.setItems(FXCollections.observableArrayList(users));
        } catch (Exception e) {
            showAlert("Error Loading Users", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void getServerPermissions() {
        try {
            permissions = userService.getAllPermissions();
        } catch (Exception e) {
            showAlert("Error Loading Permissions", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Set<PermissionDto> getPermissions() {
        return fpPermissions.getChildren().stream()
                .filter(node -> node instanceof MFXCheckbox && ((MFXCheckbox) node).isSelected())
                .map(node -> permissions.stream()
                        .filter(permission -> permission.getName().toString().equals(((MFXCheckbox) node).getText().replace(" ", "_")))
                        .findFirst().orElse(null))
                .collect(Collectors.toSet());
    }

    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }

    private void refreshTable() throws Exception {
        List<UserDto> users = userService.getAllUsers();
        tbvUser.setItems(FXCollections.observableArrayList(users));
    }

    private void deleteUser(UserDto selectedUser) {
        if (selectedUser != null) {
            try {
                if (selectedUser.getId().equals(userSession.getId()) && userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.DELETE_USERS))) {
                    showAlert("Error Deleting User", "You can't delete yourself.", Alert.AlertType.ERROR);
                    return;
                }
                Answer answer = userService.deleteUser(selectedUser.getId());
                if (!answer.getState()) showAlert("Error Deleting User", answer.getMessage(), Alert.AlertType.ERROR);
                else {
                    showAlert("Delete User", "User deleted successfully.", Alert.AlertType.INFORMATION);
                    refreshTable();
                }
                loadUsers();
            } catch (Exception e) {
                showAlert("Error deleting user", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else showAlert("Warning", "Select a user from table.", Alert.AlertType.WARNING);
        updateTable();
    }
    @FXML
    private void onActionBtnNew(ActionEvent actionEvent) {
        if (new Message().showConfirmation("New Career", getStage(), "Are you sure you want to clean the registry?")) {
            prepareForNewUser();
        }
    }

    @FXML
    void OnMouseClickedTbvUser(MouseEvent event) {
        if (event.getClickCount() == 2) {
            UserDto selectedUser = tbvUser.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                txfNumberID.setDisable(true);
                clearForm();
                userInput = new UserInput(selectedUser);
                bindUser();
            }
        }
    }

    private void updateTable() {
        try {
            loadUsers();
        } catch (Exception e) {
            showAlert("Error", "An error occurred while loading the users: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void prepareForNewUser() {
        newUser();
        setFieldsEditable(true);
    }

    private void setFieldsEditable(boolean isEditable) {
        txfNumberID.setEditable(isEditable);
        txfNumberID.setDisable(!isEditable);
        txfPassword.setEditable(isEditable);
        txfPassword.setDisable(!isEditable);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadUsers(int page) {
        try {
            Answer response = userService.getUsers(page, PAGE_SIZE);
            if (response.getState()) {
                Map<String, Object> rootData = response.getResult();
                Map<String, Object> data = (Map<String, Object>) rootData.get("");
                List<UserDto> users = objectMapper.convertValue(data.get("users"), new TypeReference<List<UserDto>>() {
                });
                long totalElements = objectMapper.convertValue(data.get("totalElements"), Long.class);
                tbvUser.getItems().setAll(users);
                int totalPages = (int) Math.ceil((double) totalElements / PAGE_SIZE);
                pagination.setPageCount(totalPages);
            } else {
                throw new Exception(response.getMessage());
            }
        } catch (Exception e) {
            showError("Error", "An error occurred while loading the users: " + e.getMessage());
        }
    }

    private Node createPage(int pageIndex) {
        loadUsers(pageIndex);
        return new VBox(tbvUser);
    }

    private void configurePagination() {
        pagination.setPageFactory(this::createPage);
        loadUsers(0);
    }
}