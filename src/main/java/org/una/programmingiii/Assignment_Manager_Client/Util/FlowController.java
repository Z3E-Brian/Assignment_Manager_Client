package org.una.programmingIII.Assignment_Manager_Client.Util;

import org.una.programmingIII.Assignment_Manager_Client.App; // Cambiar esta ruta según el paquete del nuevo proyecto

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.una.programmingIII.Assignment_Manager_Client.Controller.Controller; // Cambiar esta ruta según el paquete del nuevo proyecto
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

public class FlowController {
    private static FlowController INSTANCE = null;
    private static Stage mainStage;
    private static ResourceBundle idioma;
    private static final HashMap<String, FXMLLoader> loaders = new HashMap<>();

    private FlowController() {
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (FlowController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FlowController();
                }
            }
        }
    }

    public static FlowController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void InitializeFlow(Stage stage, ResourceBundle idioma) {
        getInstance();
        FlowController.mainStage = stage;
        FlowController.idioma = idioma;
    }

    private FXMLLoader getLoader(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader == null) {
            synchronized (FlowController.class) {
                // Mejora en la sincronización
                loader = loaders.get(name);
                if (loader == null) {
                    try {
                        loader = new FXMLLoader(App.class.getResource("view/" + name + ".fxml"), FlowController.idioma); // Cambiar la ruta de "view/" según el paquete de vistas del nuevo proyecto
                        loader.load();
                        loaders.put(name, loader);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(FlowController.class.getName())
                                .log(Level.SEVERE, "Error al crear loader [" + name + "].", ex);
                    }
                }
            }
        }
        return loader;
    }

    public void goMain() {
        try {
            Scene scene = new Scene(FXMLLoader.load(App.class.getResource("view/MainView.fxml"), FlowController.idioma)); // Cambiar "view/PrincipalView.fxml" según el proyecto
            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
            mainStage.setScene(scene);
            mainStage.show();
            mainStage.setTitle("Assignment Manager"); // Cambiar el título según el nuevo proyecto
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName())
                    .log(Level.SEVERE, "Error inicializando la vista principal.", ex);
        }
    }

    public void goView(String viewName) {
        goView(viewName, "Center", null);
    }

    public void goView(String viewName, String accion) {
        goView(viewName, "Center", accion);
    }

    public void goView(String viewName, String location, String accion) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setAccion(accion);
        controller.initialize();
        Stage stage = controller.getStage();
        if (stage == null) {
            stage = FlowController.mainStage;
            controller.setStage(stage);
        }

        // Mejorar el manejo de ubicaciones
        BorderPane root = (BorderPane) stage.getScene().getRoot();
        switch (location) {
            case "Center":
                setInVBox((VBox) root.getCenter(), loader.getRoot());
                break;
            case "Left":
                setInVBox((VBox) root.getLeft(), loader.getRoot());
                break;
            // Puedes añadir más casos según lo necesites
            default:
                java.util.logging.Logger.getLogger(FlowController.class.getName())
                        .log(Level.WARNING, "Ubicación no soportada: " + location);
                break;
        }
    }

    public void goViewInStage(String viewName, Stage stage) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.getScene().setRoot(loader.getRoot());
        MFXThemeManager.addOn(stage.getScene(), Themes.DEFAULT, Themes.LEGACY);
    }

    public void goViewInWindow(String viewName) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/una/programmingIII/Assignment_Manager_Client/Assets/Assignment-Manager.png"))));

        stage.setTitle("Assignment Manager"); // Cambiar título según el proyecto
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Scene scene = new Scene(loader.getRoot());
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.setTitle(controller.getNombreVista());
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Scene scene = new Scene(loader.getRoot());
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    public void showViewInVBox(String viewName, VBox vbox) {
        try {
            FXMLLoader loader = getLoader(viewName);
            setInVBox(vbox, loader.getRoot());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName())
                    .log(Level.SEVERE, "Error mostrando vista en VBox.", ex);
        }
    }

    private void setInVBox(VBox vbox, Parent view) {
        VBox.setVgrow(view, Priority.ALWAYS);
        if (view instanceof Region) {
            ((Region) view).setMaxWidth(Double.MAX_VALUE);
            ((Region) view).setMaxHeight(Double.MAX_VALUE);
        }
        vbox.getChildren().clear();
        vbox.getChildren().add(view);
    }

    public Controller getController(String viewName) {
        return getLoader(viewName).getController();
    }

    public void limpiarLoader(String view) {
        loaders.remove(view);
    }

    public static void setIdioma(ResourceBundle idioma) {
        FlowController.idioma = idioma;
    }

    public void initialize() {
        loaders.clear();
    }

    public void salir() {
        mainStage.close();
    }

    public void delete(String parametro) {
        loaders.remove(parametro);
    }
}
