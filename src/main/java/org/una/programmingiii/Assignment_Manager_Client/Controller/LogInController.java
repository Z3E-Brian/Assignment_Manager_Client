package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager_Client.Service.AuthenticationService;
import org.una.programmingIII.Assignment_Manager_Client.Service.FileService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogInController extends Controller {

    @FXML
    private MFXButton btnLogIn;

    @FXML
    private MFXButton btnRegister;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXTextField usernameField;

    private AuthenticationService authenticationService;
    private LoginResponse loginResponse;

    @Override
    public void initialize() {
        authenticationService = new AuthenticationService();
        usernameField.setText("admin@admin.com");
        passwordField.setText("admin");
    }

    @FXML
    void onActionBtnLogIn(ActionEvent event) {
        try {
            if (authenticateUser()) {
                SessionManager.getInstance().setLoginResponse(loginResponse);
                FlowController.getInstance().goMain();
                getStage().close();
            }
        } catch (Exception exception) {
            new Message().showModal(Alert.AlertType.ERROR, "Inicio de sesion", getStage(), exception.getMessage());
        }
    }

    @FXML
    void onActionBtnRegister(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("RegisterUserView");
        getStage().close();

    }

    @FXML
    void onActionBtnGoMain(ActionEvent event) {
        FlowController.getInstance().goMain();
        getStage().close();
    }

    @FXML
    void onActionBtnMantenimientoUnis(ActionEvent event) {
        SessionManager.getInstance().setLoginResponse(loginResponse);
        FlowController.getInstance().goViewInWindow("UniversityMaintenanceView");
        getStage().close();
    }

    private boolean authenticateUser() throws Exception {
        String email = usernameField.getText();
        String password = passwordField.getText();
        LoginInput loginInput = new LoginInput(email, password);
        loginResponse = authenticationService.authenticate(loginInput);
        return loginResponse != null;
    }


}
