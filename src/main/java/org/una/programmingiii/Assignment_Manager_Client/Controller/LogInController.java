package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

import java.io.IOException;

public class LogInController extends Controller {

    @FXML
    private MFXButton btnLogIn;

    @FXML
    private MFXButton btnRegister;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXTextField usernameField;

    @Override
    public void initialize() {
    }
    @FXML
    void onActionBtnLogIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

//            if (authenticateUser(username, password)) {
        FlowController.getInstance().goMain();
        getStage().close();
        //}
    }

    @FXML
    void onActionBtnRegister(ActionEvent event) {
FlowController.getInstance().goViewInWindow("UniversityMaintenanceView");
    }

    private boolean authenticateUser(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }
}
