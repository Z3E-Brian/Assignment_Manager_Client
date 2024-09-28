/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.programmingiii.assignment_manager_client.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.una.programmingiii.assignment_manager_client.App;
import org.una.programmingiii.assignment_manager_client.Controller.Controller;

public class FlowController {

    private static Stage mainStage;
    private static ResourceBundle idioma;
    private static final HashMap<String, FXMLLoader> loaders = new HashMap<>();
    private static Controller controller;

    private FlowController() {
    }

    private static final class InstanceHolder {
        private static final FlowController INSTANCE = new FlowController();
    }


    public static FlowController getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void InitializeFlow(Stage stage, ResourceBundle nIdioma) {
        //getInstance();
        mainStage = stage;
        idioma = nIdioma;
    }

    private FXMLLoader getLoader(String name) {
        FXMLLoader loader = loaders.get(name);
        if (loader == null) {
            synchronized (FlowController.class) {
                try {
                    loader = new FXMLLoader(App.class.getResource("view/" + name + ".fxml"), idioma);
                    loader.load();
                    loaders.put(name, loader);
                } catch (Exception ex) {
                    loader = null;
                    java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Creando loader [" + name + "].", ex);
                }
            }
        }
        if (!name.equals("LogIng2")) {
            assert loader != null;
            controller = Objects.requireNonNull(loader).getController();
        }
        return loader;
    }

    public void goMain() {
        try {
            mainStage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(App.class.getResource("org/una/programmingiii/assignment_manager_client/Controller/HelloController.java")), idioma)));
            mainStage.show();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FlowController.class.getName()).log(Level.SEVERE, "Error inicializando la vista base.", ex);
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
            stage = mainStage;
            controller.setStage(stage);
        }
        switch (location) {
            case "Center":
                ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().clear();
                ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().add(loader.getRoot());
                break;
            case "Top":
                break;
            case "Bottom":
                break;
            case "Right":
                break;
            case "Left":
                break;
            default:
                break;
        }
    }

    public void goViewInStage(String viewName, Stage stage) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.setStage(stage);
        stage.getScene().setRoot(loader.getRoot());
    }

    public void goViewInWindow(String viewName) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.setTitle("Assignment Manager");
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    public void goLogInWindowModal(Boolean resizable) {
        goViewInWindowModal("LogIng2", this.controller.getStage(), resizable);
    }

    public void goViewInWindowModal(String viewName, Stage parentStage, Boolean resizable) {
        FXMLLoader loader = getLoader(viewName);
        Controller controller = loader.getController();
        controller.initialize();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/cr/ac/una/unaplanilla/resources/Usuario-48.png")));
        stage.setTitle("UNA PLANILLA");
        stage.setResizable(resizable);
        stage.setOnHidden((WindowEvent event) -> {
            controller.getStage().getScene().setRoot(new Pane());
            controller.setStage(null);
        });
        controller.setStage(stage);
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parentStage);
        stage.centerOnScreen();
        stage.showAndWait();

    }

    public Controller getController(String viewName) {
        return getLoader(viewName).getController();
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

}