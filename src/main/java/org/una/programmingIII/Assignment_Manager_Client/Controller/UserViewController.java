package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.una.programmingIII.Assignment_Manager_Client.Model.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Model.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Model.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.util.ArrayList;
import java.util.List;

public class UserViewController extends Controller {

    @FXML
    private TextField nameField, emailField;
    @FXML
    private Button createButton, updateButton, deleteButton, getAllButton;
    @FXML
    private TableView<UserDto> userTable;
    @FXML
    private TableColumn<UserDto, Long> idColumn;
    @FXML
    private TableColumn<UserDto, String> nameColumn, emailColumn;

    private final UserService userService = new UserService();

    @Override
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            ObservableList<UserDto> userList = FXCollections.observableArrayList(users);
            userTable.setItems(userList);
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    private void createUser() {
        //TODO: CURRENTLY DONT SHOW ANYTHING ABOUT ERRORS NEITHER SUCCESS AND ADD REMAINING FIELDS
        UserInput userInput = new UserInput();
        userInput.setName(nameField.getText());
        userInput.setEmail(emailField.getText());
        userInput.setPassword("1234");
        userInput.setRole(new ArrayList<>(List.of(PermissionType.MANAGE_USERS, PermissionType.VIEW_COURSES, PermissionType.SUBMIT_ASSIGNMENTS)));
        userInput.setIdentificationNumber("4444");
        try {
            userService.createUser(userInput);
            loadUsers();
            clearForm();
        } catch (Exception e) {
            showAlert("Error creating the user", e.getMessage());
        }
    }

    @FXML
    private void updateUser() {
        UserDto selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UserInput userInput = new UserInput();
            userInput.setName(nameField.getText());
            userInput.setEmail(emailField.getText());
            try {
                userService.updateUser(selectedUser.getId(), userInput);
                loadUsers();
                clearForm();
            } catch (Exception e) {
                showAlert("Error updating user", e.getMessage());
            }
        } else {
            showAlert("Warning", "Select a user from table.");
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
                showAlert("Error deleting user", e.getMessage());
            }
        } else {
            showAlert("Warning", "Select a user from table.");
        }
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        userTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void getAll(ActionEvent actionEvent) {
        loadUsers();
    }
}