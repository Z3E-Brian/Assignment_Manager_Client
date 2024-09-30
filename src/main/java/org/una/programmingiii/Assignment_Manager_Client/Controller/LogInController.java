package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

public class LogInController extends Controller {

    @FXML
    private MFXTextField usernameField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXButton loginButton;

    @FXML
    private MFXButton registerButton;

    @FXML
    private VBox loginVBox; // Asumamos que estás usando un VBox para el layout

    @Override
    public void initialize() {
        // Configurar la lógica del controlador al iniciar

        // Acción para el botón de "Iniciar Sesión"
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (authenticateUser(username, password)) {
                System.out.println("Login exitoso!");
                FlowController.getInstance().goMain(); // Navegar a la vista principal
            } else {
                System.out.println("Credenciales incorrectas.");
                // Aquí puedes mostrar un mensaje de error o una alerta
            }
        });

        // Acción para el botón de "Registrarse"
        registerButton.setOnAction(event -> {
            // Lógica para ir a la ventana de registro
            FlowController.getInstance().goViewInWindowModal("RegisterView", getStage(), false); // Navegar a una ventana modal para registrarse
        });
    }

    // Método de autenticación (ejemplo simple)
    private boolean authenticateUser(String username, String password) {
        // Lógica de autenticación real, por ejemplo, consulta a la base de datos o API
        return username.equals("admin") && password.equals("admin");
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage); // Usa el método del padre para configurar el Stage
    }

    @Override
    public void setAccion(String accion) {
        super.setAccion(accion); // Usa el método del padre para configurar la acción
    }

    @Override
    public void setNombreVista(String nombreVista) {
        super.setNombreVista(nombreVista); // Usa el método del padre para configurar el nombre de la vista
    }

}
