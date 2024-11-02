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
    public MFXButton btnNew, btnSearch, btnSave, btnDelete, btnClear;
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
        numberIDColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        PermissionType[] permissions = PermissionType.values();
        for (PermissionType permission : permissions) {
            MFXCheckbox checkBox = new MFXCheckbox(permission.toString().replace("_", " "));
            checkBox.setPrefWidth(180);
            fpPermissions.getChildren().add(checkBox);
        }
        bindUser();
        loadUsers();
    }

    private void bindUser() {
        unbindUser();
        txfName.textProperty().bindBidirectional(userInput.name);
        txfEmail.textProperty().bindBidirectional(userInput.email);
        txfNumberID.textProperty().bindBidirectional(userInput.identificationNumber);
        txfLastName.textProperty().bindBidirectional(userInput.lastName);
        txfSecondLastName.textProperty().bindBidirectional(userInput.secondLastName);
        txfPassword.textProperty().bindBidirectional(userInput.password);
    }

    private void unbindUser() {
        txfName.textProperty().unbindBidirectional(userInput.name);
        txfEmail.textProperty().unbindBidirectional(userInput.email);
        txfNumberID.textProperty().unbindBidirectional(userInput.identificationNumber);
        txfLastName.textProperty().unbindBidirectional(userInput.lastName);
        txfSecondLastName.textProperty().unbindBidirectional(userInput.secondLastName);
        txfPassword.textProperty().unbindBidirectional(userInput.password);
    }

    private void newUser() {
        userInput = new UserInput();
        unbindUser();
        bindUser();
        txfName.requestFocus();
    }

    private void initiatePermissions() {
        List<String> permissions = Arrays.stream(PermissionType.values()).map(PermissionType::name).toList();
        for (String permission : permissions) {
            MFXCheckbox checkBox = new MFXCheckbox(permission.replace("_", " "));
            checkBox.setPrefWidth(150);
            fpPermissions.getChildren().add(checkBox);
        }
    }

    private void configureTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        secondLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondLastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        numberIDColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        userTable.setItems(FXCollections.observableArrayList());
    }

    private void configureInputFormat() {
        // Uncomment if needed
        // txfName.delegateSetTextFormatter(Format.getInstance().textFormat(100));
        // txfEmail.delegateSetTextFormatter(Format.getInstance().textFormat(100));
        // txfNumberID.delegateSetTextFormatter(Format.getInstance().integerFormat(20));
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        bindUser();
        NewUserDto newUserDto = new NewUserDto(userInput);
        newUserDto.setPermissions(getPermissions());
        Answer answer = userService.createUser(newUserDto);
        if (!answer.getState()) {
            System.out.println(answer.getMessage());
            showError("Create User", answer.getMessage());
        } else {
            showInfo("Create User", "User created successfully.");
            newUser();
            refreshTable();
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

    @FXML
    private void createUser() {
        if (!validateInput()) return;
        userInput.setRole(fpPermissions.getChildren().stream()
                .filter(node -> node instanceof MFXCheckbox && ((MFXCheckbox) node).isSelected())
                .map(node -> PermissionType.valueOf(((MFXCheckbox) node).getText()))
                .collect(Collectors.toList()));
        try {
            loadUsers();
            clearForm();
        } catch (Exception e) {
            showAlert("Error creating the user", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateInput() {
        if (txfNumberID.getText().isEmpty()) {
            showAlert("Validation Error", "ID field is required.", Alert.AlertType.ERROR);
            return false;
        }
        if (txfName.getText().isEmpty()) {
            showAlert("Validation Error", "Name field is required.", Alert.AlertType.ERROR);
            return false;
        }
        if (txfEmail.getText().isEmpty()) {
            showAlert("Validation Error", "Email field is required.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void updateUser() {
        UserDto selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
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
        // Clear form logic if needed
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
        String enteredEmail = txfEmail.getText();
        if (enteredEmail.isEmpty()) {
            showAlert("Validation Error", "Please enter an ID or identification number.", Alert.AlertType.ERROR);
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
        // Bind user to form logic if needed
    }

    @FXML
    private void prepareForNewUser() {
        clearForm();
        setFieldsEditable(true);
    }

    private void setFieldsEditable(boolean isEditable) {
        txfNumberID.setEditable(isEditable);
        txfNumberID.setDisable(!isEditable);
    }

    private Set<PermissionDto> getPermissions() {
        Set<PermissionDto> permissions = new HashSet<>();
        for (Node node : fpPermissions.getChildren()) {
            if (node instanceof MFXCheckbox checkBox && checkBox.isSelected()) {
                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setName(PermissionType.valueOf(checkBox.getText().replace(" ", "_")));
                permissions.add(permissionDto);
            }
        }
        return permissions;
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
    public void onActionBtnDelete(ActionEvent actionEvent) {
    }

    public void onActionBtnClear(ActionEvent actionEvent) {
    }

    public void onActionBtnSearch(ActionEvent actionEvent) {
    }

    public void onActionBtnNew(ActionEvent actionEvent) {
    }

}