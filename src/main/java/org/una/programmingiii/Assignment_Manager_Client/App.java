package org.una.programmingIII.Assignment_Manager_Client;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Inicializa el FlowController sin un archivo de recursos
        FlowController.getInstance().InitializeFlow(stage, null);

        // Establece el título de la ventana
        stage.setTitle("Assignment Manager");

        // Agrega el icono a la ventana
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/una/programmingIII/Assignment_Manager_Client/Assets/Assignment-Manager.png"))));

        // Cargar la vista principal en la ventana
        FlowController.getInstance().goMain();

        // Mostrar la ventana principal
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
