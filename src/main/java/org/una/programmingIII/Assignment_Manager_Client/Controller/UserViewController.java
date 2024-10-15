package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.util.ArrayList;
import java.util.List;

public class UserViewController extends Controller {

    @FXML
    private MFXTextField nameField, emailField,IDField,numberIDField;
    @FXML
    private Button createButton, updateButton, deleteButton, getAllButton;
    @FXML
    private TableView<UserDto> userTable;
    @FXML
    private TableColumn<UserDto,Long> idColumn;
    @FXML
    private TableColumn<UserDto,String> nameColumn, emailColumn, numberIDColumn;

    private final UserService userService = new UserService();

    @Override
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numberIDColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            ObservableList<UserDto> userList = FXCollections.observableArrayList(users);
            userTable.setItems(userList);
        } catch (java.net.ConnectException e) {
            showAlert("Connection Error", "Failed to connect to the server. Please make sure the server is running.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error Loading Users", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void createUser() {
        if (!validateInput()) {
            return;
        }
        UserInput userInput = new UserInput();
        userInput.setIdentificationNumber(numberIDField.getText());
        userInput.setName(nameField.getText());
        userInput.setEmail(emailField.getText());
        userInput.setPassword("1234");
        userInput.setRole(new ArrayList<>(List.of(PermissionType.MANAGE_USERS, PermissionType.VIEW_COURSES, PermissionType.SUBMIT_ASSIGNMENTS)));
        try {
            userService.createUser(userInput);
            loadUsers();
            clearForm();
        } catch (Exception e) {
            showAlert("Error creating the user", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private boolean validateInput() {
        if (numberIDField.getText() == null || numberIDField.getText().isEmpty()) {
            showAlert("Validation Error", "ID field is required.", Alert.AlertType.ERROR);
            return false;
        }
        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            showAlert("Validation Error", "Name field is required.", Alert.AlertType.ERROR);
            return false;
        }
        if (emailField.getText() == null || emailField.getText().isEmpty()) {
            showAlert("Validation Error", "Email field is required.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }


    @FXML
    private void updateUser() {
        UserDto selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UserInput userInput = new UserInput();
            userInput.setIdentificationNumber(numberIDField.getText());
            userInput.setName(nameField.getText());
            userInput.setEmail(emailField.getText());
            try {
                userService.updateUser(selectedUser.getId(), userInput);
                loadUsers();
                clearForm();
            } catch (Exception e) {
                showAlert("Error updating user", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Select a user from table.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void deleteUser() {
        UserDto selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userService.deleteUser(selectedUser.getId());
                loadUsers();
                clearForm();
            } catch (Exception e) {
                showAlert("Error deleting user", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Select a user from table.", Alert.AlertType.WARNING);
        }
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        userTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void getAll(ActionEvent actionEvent) {
        loadUsers();
    }
}