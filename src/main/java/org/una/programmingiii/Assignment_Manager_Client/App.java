package org.una.programmingIII.Assignment_Manager_Client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage,null);
        stage.setTitle("Assignment Manager");
        FlowController.getInstance().goViewInWindow("LogInView");
    }

    public static void main(String[] args) {
        launch();
    }
}
