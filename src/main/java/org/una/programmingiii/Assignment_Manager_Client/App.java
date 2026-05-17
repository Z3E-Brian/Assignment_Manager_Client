package org.una.programmingIII.Assignment_Manager_Client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

public class  App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowController.getInstance().InitializeFlow(primaryStage, null);
        FlowController.getInstance().goViewInWindow("LogInView");
    }

    public static void main(String[] args) {
        launch();
    }
}

