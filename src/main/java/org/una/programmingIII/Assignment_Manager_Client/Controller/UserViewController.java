package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.apache.hc.core5.http.Header;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class UserViewController extends Controller {

    @FXML
    public FlowPane fpPermissions;
    @FXML
    public HBox hBoxID;
    @FXML
    private MFXTextField nameField, emailField,IDField,numberIDField;
    @FXML
    private Button createButton, updateButton, deleteButton, getAllButton,getUploadButton;
    @FXML
    private TableView<UserDto> userTable;
    @FXML
    private TableColumn<UserDto,Long> idColumn;
    @FXML
    private TableColumn<UserDto,String> nameColumn, emailColumn, numberIDColumn;

    private final UserService userService = new UserService();
    private final UserInput userInput = new UserInput();
    @Override
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numberIDColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        numberIDField.textProperty().bindBidirectional(userInput.getIdentificationNumber());
        nameField.textProperty().bindBidirectional(userInput.getName());
        emailField.textProperty().bindBidirectional(userInput.getEmail());

        List<PermissionType> permissions = Arrays.asList(PermissionType.values());
        for (PermissionType permission : permissions) {
            MFXCheckbox checkBox = new MFXCheckbox(permission.toString());
            fpPermissions.getChildren().add(checkBox);
        }

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
        userInput.setPassword("1234"); // Contraseña por defecto

        // Recoger permisos seleccionados
        userInput.setRole(fpPermissions.getChildren().stream()
                .filter(node -> node instanceof MFXCheckbox && ((MFXCheckbox) node).isSelected())
                .map(node -> PermissionType.valueOf(((MFXCheckbox) node).getText()))
                .collect(Collectors.toList()));

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
//            userInput.setIdentificationNumber(numberIDField.getText());
//            userInput.setName(nameField.getText());
//            userInput.setEmail(emailField.getText());
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
        userInput.getName().set("");
        userInput.getEmail().set("");
        userInput.getIdentificationNumber().set("");
        userTable.getSelectionModel().clearSelection();
        setFieldsEditable(true);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void loadUserByEmail() {
        String enteredEmail = emailField.getText();
        if (enteredEmail.isEmpty()) {
            showAlert("Validation Error", "Please enter an ID or identification number.", Alert.AlertType.ERROR);
            return;
        }

        try {
            UserDto user = userService.getUserByEmail(enteredEmail);
            if (user != null) {
                bindUserToForm(user); // Llenar el formulario con los datos del usuario
                setFieldsEditable(false); // Deshabilitar edición de ID/cédula
            } else {
                showAlert("User Not Found", "No user found with the provided ID.", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred while loading the user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void bindUserToForm(UserDto user) {
        userInput.getName().set(user.getName());
        userInput.getEmail().set(user.getEmail());
        userInput.getIdentificationNumber().set(user.getIdentificationNumber());
    }
    @FXML
    private void prepareForNewUser() {
        clearForm();  // Limpiar el formulario para un nuevo usuario
        setFieldsEditable(true);  // Habilitar los campos de cédula/ID
    }

    private void setFieldsEditable(boolean isEditable) {
        numberIDField.setEditable(isEditable); // Permite o no editar el campo de ID
        IDField.setEditable(isEditable);       // Permite o no editar otros campos si es necesario
        numberIDField.setDisable(!isEditable); // Deshabilita o habilita el campo de ID
        IDField.setDisable(!isEditable);       // Deshabilita o habilita otros campos si es necesario

    }



    @FXML
    private void getAll(ActionEvent actionEvent) {
        loadUsers();
    }
}