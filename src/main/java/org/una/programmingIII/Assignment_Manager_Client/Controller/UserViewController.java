package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.NewUserDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class UserViewController extends Controller implements Initializable {

    @FXML
    private MFXButton btnNew, btnSearch, btnSave, btnDelete, btnClear;
    @FXML
    private MFXTextField txfNumberID, txfName, txfLastName, txfSecondLastName, txfEmail, txfPassword;
    @FXML
    private FlowPane fpPermissions;
    @FXML
    private TableView<UserDto> userTable;
    @FXML
    private TableColumn<UserDto, String> nameColumn, lastNameColumn, secondLastNameColumn, emailColumn, numberIDColumn;

    private final UserService userService = new UserService();
    private UserInput userInput = new UserInput();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    @Override
    public void initialize() {
        configureTable();
        initializePermissions();
        bindUser();
        loadUsers();
    }

    private void configureTable() {
        numberIDColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        secondLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondLastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void initializePermissions() {
        for (PermissionType permission : PermissionType.values()) {
            MFXCheckbox checkBox = new MFXCheckbox(permission.toString().replace("_", " "));
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
        //bindear los checkboxes
        for (Node node : fpPermissions.getChildren()) {
            if (node instanceof MFXCheckbox checkBox) {
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        PermissionType permissionDto = PermissionType.valueOf(checkBox.getText().replace(" ", "_"));
                        userInput.role.add(permissionDto);
                    } else {
                        userInput.role.removeIf(permissionDto -> permissionDto.name().equals(checkBox.getText().replace(" ", "_")));
                    }
                });
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
        //unbind checkboxes
        for (Node node : fpPermissions.getChildren()) {
            if (node instanceof MFXCheckbox checkBox) {
                checkBox.selectedProperty().removeListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        PermissionType permissionDto = PermissionType.valueOf(checkBox.getText().replace(" ", "_"));
                        userInput.role.add(permissionDto);
                    } else {
                        userInput.role.removeIf(permissionDto -> permissionDto.name().equals(checkBox.getText().replace(" ", "_")));
                    }
                });
            }
        }
    }

    private void newUser() {
        userInput = new UserInput();
        unbindUser();
        bindUser();
//        clearForm();
        setFieldsEditable(true);
        txfName.requestFocus();
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (txfNumberID.isDisabled()) {
            updateUser();
        } else {
            createUser();
        }
    }

    private void createUser() throws Exception {
        bindUser();
        NewUserDto newUserDto = new NewUserDto(userInput);
        newUserDto.setPermissions(getPermissions());
        Answer answer = userService.createUser(newUserDto);
        if (!answer.getState()) {
            showError("Create User", answer.getMessage());
        } else {
            showInfo("Create User", "User created successfully.");
            newUser();
            refreshTable();
        }
    }

    private void updateUser() throws Exception {
        UserDto selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userInput.setId(selectedUser.getId());
            NewUserDto newUserDto = new NewUserDto(userInput);
            newUserDto.setPermissions(getPermissions());
            Answer answer = userService.updateUser(selectedUser.getId(), newUserDto);
            if (!answer.getState()) {
                showError("Update User", answer.getMessage());
            } else {
                showInfo("Update User", "User updated successfully.");
                newUser();
                refreshTable();
            }
        } else {
            showError("Update User", "Please select a user to update.");
        }
    }

    private void loadUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            userTable.setItems(FXCollections.observableArrayList(users));
        } catch (Exception e) {
            showAlert("Error Loading Users", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Set<PermissionDto> getPermissions() {
        return null;
//        return fpPermissions.getChildren().stream()
//                .filter(node -> node instanceof MFXCheckbox && ((MFXCheckbox) node).isSelected())
//                .map(node -> {
//                    PermissionDto permissionDto = new PermissionDto();
//                    permissionDto.setName(PermissionType.valueOf(((MFXCheckbox) node).getText().replace(" ", "_")));
//                    return permissionDto;
//                })
//                .collect(Collectors.toSet());
    }

    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }

    private void refreshTable() throws Exception {
        List<UserDto> users = userService.getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    private void onActionBtnDelete(ActionEvent actionEvent) {
        deleteUser();
    }

    private void deleteUser() {
        UserDto selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userService.deleteUser(selectedUser.getId());
                loadUsers();
//                clearForm();
            } catch (Exception e) {
                showAlert("Error deleting user", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Select a user from table.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onActionBtnClear(ActionEvent actionEvent) {
//        clearForm();
    }

//    private void clearForm() {
//        // Clear form logic if needed
//    }

    @FXML
    private void onActionBtnSearch(ActionEvent actionEvent) {
        loadUserByEmail();
    }

    private void loadUserByEmail() {
        String enteredEmail = txfEmail.getText();
        if (enteredEmail.isEmpty()) {
            showAlert("Validation Error", "Please enter an Email.", Alert.AlertType.ERROR);
            return;
        }

        try {
            UserDto user = userService.getUserByEmail(enteredEmail);
            if (user != null) {
                bindUserToForm(user);
                setFieldsEditable(false);
            } else {
                showAlert("User Not Found", "No user found with the provided ID.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred while loading the user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void bindUserToForm(UserDto user) {
        txfNumberID.setText(user.getIdentificationNumber());
        txfName.setText(user.getName());
        txfLastName.setText(user.getLastName());
        txfSecondLastName.setText(user.getSecondLastName());
        txfEmail.setText(user.getEmail());
        txfPassword.setText("********");
        user.getPermissions().forEach(permission -> {
            for (Node node : fpPermissions.getChildren()) {
                if (node instanceof MFXCheckbox checkBox && checkBox.getText().equals(permission.getName().toString().replace("_", " "))) {
                    checkBox.setSelected(true);
                }
            }
        });
    }

    @FXML
    private void onActionBtnNew(ActionEvent actionEvent) {
        prepareForNewUser();
    }

    private void prepareForNewUser() {
//        clearForm();
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
}