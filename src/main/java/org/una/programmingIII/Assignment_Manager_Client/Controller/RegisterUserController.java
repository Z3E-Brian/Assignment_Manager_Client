package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.AuthenticationService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;
import org.una.programmingIII.Assignment_Manager_Client.Util.Format;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterUserController extends Controller {

    @FXML
    private MFXButton btnLogIn;

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
    private MFXTextField usernameField;

    @FXML
    private MFXButton btnSendEmail;

    private UserService userService;
    private AuthenticationService authenticationService;
    private UserInput userInput;
    List<Node> requeridos = new ArrayList<>();


    @Override
    public void initialize() {
        userService = new UserService();
        userInput = new UserInput();
        authenticationService = new AuthenticationService();

        usernameField.delegateSetTextFormatter(Format.getInstance().textFormat(80));
        identificationNumberField.delegateSetTextFormatter(Format.getInstance().textFormat(8));
        emailField.delegateSetTextFormatter(Format.getInstance().textFormat(40));

        identificationNumberField.textProperty().bindBidirectional(userInput.getIdentificationNumber());
        usernameField.textProperty().bindBidirectional(userInput.getName());
        emailField.textProperty().bindBidirectional(userInput.getEmail());
        passwordField.textProperty().bindBidirectional(userInput.getPassword());

        indicateRequeridos();
        clean();
        emailField.setText("justin77mendezmena@gmail.com");
    }

    @FXML
    void onActionBtnLogIn(ActionEvent event) {
        clean();
        FlowController.getInstance().goViewInWindow("LogInView");
        FlowController.getInstance().delete("RegisterUserView");
        getStage().close();
    }

    @FXML
    void onActionBtnRegister(ActionEvent event) {
        if (authenticationService.checkVerificationClick()) {
            System.out.println("verification clicked");
        } else {
            new Message().showModal(Alert.AlertType.ERROR, "Registrar Usuario", getStage(), "Existen espacios  importantes en blanco");
        }
        //userService.createUser(userInput);
    }

    @FXML
    void onActionBtnSendEmail(ActionEvent event) throws Exception {
        if (!emailField.getText().isBlank()) {
            authenticationService.sendVerificationEmail(emailField.getText());
        } else {
            new Message().showModal(Alert.AlertType.ERROR, "Registrar Usuario", getStage(), "Existen espacios  importantes en blanco");
        }
    }

    private void indicateRequeridos() {
        requeridos.addAll(Arrays.asList(emailField, passwordField, usernameField, confirmPasswordField));
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
        usernameField.setText("");
        confirmPasswordField.setText("");

    }


}
