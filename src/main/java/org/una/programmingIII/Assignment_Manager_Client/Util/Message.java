package org.una.programmingIII.Assignment_Manager_Client.Util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

/**
 *
 * @author ccarranza
 */
public class Message {

    public void show(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void showModal(AlertType type, String title, Window padre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(padre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Boolean showConfirmation(String title, Window padre, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.initOwner(padre);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }
}