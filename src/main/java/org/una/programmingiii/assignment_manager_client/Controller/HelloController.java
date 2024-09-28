package org.una.programmingiii.assignment_manager_client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {welcomeText.setText("Welcome to JavaFX Application!");}
}