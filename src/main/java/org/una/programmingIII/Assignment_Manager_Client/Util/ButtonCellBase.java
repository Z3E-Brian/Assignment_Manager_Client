package org.una.programmingIII.Assignment_Manager_Client.Util;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;

public abstract class ButtonCellBase <T>extends TableCell<T, Boolean> {

    protected final MFXButton cellButton;
    protected final ImageView imageView = new ImageView();

    public ButtonCellBase(String buttonText, String styleClass) {
        this.cellButton = new MFXButton(buttonText);
        this.imageView.setFitHeight(25);
        this.imageView.setFitWidth(25);
        this.cellButton.setGraphic(imageView);
        this.cellButton.getStyleClass().add(styleClass);

        this.cellButton.setOnAction(this::handleAction);
    }

    protected abstract void handleAction(ActionEvent event);

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {
            setGraphic(cellButton);
        } else {
            setGraphic(null);
        }
    }
}
