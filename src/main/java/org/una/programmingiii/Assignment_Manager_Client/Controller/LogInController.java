package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.io.IOException;

public class LogInController extends Controller {

    @FXML
    private MFXTextField usernameField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXButton loginButton;

    @FXML
    private MFXButton registerButton;

    @Override
    public void initialize() {
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

//            if (authenticateUser(username, password)) {
                System.out.println("Login exitoso!");
                try {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setResizable(true);
                    goToView("MainView");
                    centerStage(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        });

        registerButton.setOnAction(event -> {
            try {
                Stage stage = (Stage) registerButton.getScene().getWindow();

                goToView("RegisterView");
                centerStage(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void goToView(String viewName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/una/programmingIII/Assignment_Manager_Client/View/"+viewName+".fxml"));
        getStage().setTitle("Assignment Manager");
        getStage().setScene(new javafx.scene.Scene(loader.load()));
        Controller controller = loader.getController();
        controller.setStage(getStage());
        getStage().show();
    }



    private boolean authenticateUser(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }
}
