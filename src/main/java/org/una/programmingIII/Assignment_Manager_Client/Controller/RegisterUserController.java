package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.una.programmingIII.Assignment_Manager_Client.Dto.NewUserDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.Arrays;

public class RegisterUserController extends Controller {

    @FXML
    private MFXButton btnRegister;

    @FXML
    private MFXPasswordField confirmPasswordField;

    @FXML
    private MFXTextField emailField;

    @FXML
    private MFXTextField identificationNumberField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXTextField txfName;

    @FXML
    private MFXTextField txfLastName;

    @FXML
    private MFXTextField txfSecondLastName;

    @FXML
    private MFXButton btnSendEmail;

    private UserService userService;
    private UserInput userInput;
    private NewUserDto newUserDto;
    private RequiredFieldsValidator validator;

@Override
public void initialize() {
    initializeDto();
    initializeServices();
    setupTextFormatters();
    setupValidator();
    clean();
    bind();
}

private void initializeDto() {
    newUserDto = new NewUserDto();
    userInput = new UserInput();
}

private void initializeServices() {
    userService = new UserService();
}

private void setupTextFormatters() {
    txfName.delegateSetTextFormatter(Format.getInstance().textFormat(80));
    txfLastName.delegateSetTextFormatter(Format.getInstance().textFormat(50));
    txfSecondLastName.delegateSetTextFormatter(Format.getInstance().textFormat(50));
    passwordField.delegateSetTextFormatter(Format.getInstance().textFormat(40));
    identificationNumberField.delegateSetTextFormatter(Format.getInstance().idFormat(9));
    emailField.delegateSetTextFormatter(Format.getInstance().textFormat(40));
    confirmPasswordField.delegateSetTextFormatter(Format.getInstance().textFormat(40));
}

private void setupValidator() {
    validator = new RequiredFieldsValidator(Arrays.asList(
        txfName, txfLastName, txfSecondLastName, passwordField, emailField, confirmPasswordField
    ));
}

    private void bind() {
        txfName.textProperty().bindBidirectional(userInput.name);
        txfLastName.textProperty().bindBidirectional(userInput.lastName);
        txfSecondLastName.textProperty().bindBidirectional(userInput.secondLastName);
        passwordField.textProperty().bindBidirectional(userInput.password);
        emailField.textProperty().bindBidirectional(userInput.email);
        identificationNumberField.textProperty().bindBidirectional(userInput.identificationNumber);
    }

    @FXML
    void onActionBtnRegister(ActionEvent event) throws Exception {
        String req = validator.validate();
        if (req.isBlank()) {
            if (passwordField.getText().equals(confirmPasswordField.getText())) {
                newUserDto = new NewUserDto(userInput);
                Answer response = userService.createUser(newUserDto);

                if (response.getState()) {
                    new Message().showModal(Alert.AlertType.CONFIRMATION, "Authentication", getStage(),
                            "Your account has been created, please activate your account and login.");
                    FlowController.getInstance().goViewInWindow("LoginView");
                    getStage().close();
                } else {
                    String errorMessage = response.getMessage();
                    new Message().showModal(Alert.AlertType.ERROR, "Register User", getStage(), errorMessage);
                }
            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Register User", getStage(), "Passwords do not match.");
            }
        } else {
            new Message().showModal(Alert.AlertType.ERROR, "Register User", getStage(), req);
        }
    }


    private void clean() {
        passwordField.setText("");
        emailField.setText("");
        txfName.setText("");
        txfSecondLastName.setText("");
        txfLastName.setText("");
        confirmPasswordField.setText("");

    }


}
