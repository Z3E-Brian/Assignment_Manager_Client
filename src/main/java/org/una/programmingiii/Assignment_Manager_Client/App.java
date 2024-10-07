package org.una.programmingIII.Assignment_Manager_Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import javafx.scene.image.Image;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

import java.io.InputStream;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowController.getInstance().InitializeFlow(primaryStage, null);
        FlowController.getInstance().goViewInWindow("LogInView");
    }

    public static void main(String[] args) {
        launch();
    }
}

