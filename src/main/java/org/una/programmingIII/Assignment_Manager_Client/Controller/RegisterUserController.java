package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import org.una.programmingIII.Assignment_Manager_Client.Dto.NewUserDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.AuthenticationService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    List<Node> requeridos = new ArrayList<>();


    @Override
    public void initialize() {
        newUserDto = new NewUserDto();
        userService = new UserService();
        userInput = new UserInput();

        txfName.delegateSetTextFormatter(Format.getInstance().textFormat(80));
        txfLastName.delegateSetTextFormatter(Format.getInstance().textFormat(50));
        txfSecondLastName.delegateSetTextFormatter(Format.getInstance().textFormat(50));
        passwordField.delegateSetTextFormatter(Format.getInstance().textFormat(80));
        identificationNumberField.delegateSetTextFormatter(Format.getInstance().idFormat(8));
        emailField.delegateSetTextFormatter(Format.getInstance().textFormat(40));

        indicateRequeridos();
        clean();
        bind();
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
        String req = validarRequeridos();
        if (req.isBlank()) {
            if (passwordField.getText().equals(confirmPasswordField.getText())) {
                newUserDto = new NewUserDto(userInput);
                Answer response = userService.createUser(newUserDto);

                if (response.getState()) {
                    new Message().showModal(Alert.AlertType.CONFIRMATION, "Autenticacion", getStage(),
                            "Su cuenta ha sido creada, por favor active su cuenta y inicie sesión.");
                    FlowController.getInstance().goViewInWindow("LoginView");
                    getStage().close();
                } else {
                    String errorMessage = response.getMessage();
                    new Message().showModal(Alert.AlertType.ERROR, "Registrar Usuario", getStage(), errorMessage);
                }
            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Registrar Usuario", getStage(), "Las contraseñas no coinciden.");
            }
        } else {
            new Message().showModal(Alert.AlertType.ERROR, "Registrar Usuario", getStage(),
                    "Existen espacios importantes en blanco");
        }
    }

    private void indicateRequeridos() {
        requeridos.addAll(Arrays.asList(emailField, passwordField, txfName, txfLastName, confirmPasswordField));
    }

    public String validarRequeridos() {
        boolean validos = true;
        String invalidos = "";
        for (Node node : requeridos) {
            if (node instanceof MFXTextField && (((MFXTextField) node).getText() == null || ((MFXTextField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXTextField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXTextField) node).getFloatingText();
                }
                validos = false;
            } else if (node instanceof MFXPasswordField && (((MFXPasswordField) node).getText() == null || ((MFXPasswordField) node).getText().isBlank())) {
                if (validos) {
                    invalidos += ((MFXPasswordField) node).getFloatingText();
                } else {
                    invalidos += "," + ((MFXPasswordField) node).getFloatingText();
                }
                validos = false;
            }
        }
        if (validos) {
            return "";
        } else {
            return "Campos requeridos o con problemas de formato [" + invalidos + "].";
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
