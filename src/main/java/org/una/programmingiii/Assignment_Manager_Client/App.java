package org.una.programmingIII.Assignment_Manager_Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import javafx.scene.image.Image;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/una/programmingIII/Assignment_Manager_Client/View/LogInView.fxml"));
        primaryStage.setTitle("Assignment Manager");


        // Establecer el icono personalizado
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/una/programmingIII/Assignment_Manager_Client/Assets/Assignment-Manager.png"))));

        primaryStage.setResizable(false);

        primaryStage.setScene(new javafx.scene.Scene(loader.load()));

        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

