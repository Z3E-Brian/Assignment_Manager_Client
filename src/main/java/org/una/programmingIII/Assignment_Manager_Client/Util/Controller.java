package org.una.programmingIII.Assignment_Manager_Client.Util;

import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public abstract class Controller {

    private Stage stage;
    private String accion;
    private String nombreVista;

    public void sendTabEvent(KeyEvent event) {
        event.consume();
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, null, null, KeyCode.TAB, false, false, false, false);
        ((Control) event.getSource()).fireEvent(keyEvent);
    }

    public boolean validaListaAyuda(KeyEvent event) {
        if (event.getCode() == KeyCode.F9) {
            try {
                TextInputControl control = (TextInputControl) event.getSource();
                return control.isEditable() && !control.isDisable();
            } catch (Exception ex) {
                try {
                    Control control = (Control) event.getSource();
                    return !control.isDisable();
                } catch (Exception exc) {
                    return false;
                }
            }
        }
        return false;
    }

    public abstract void initialize();

}
