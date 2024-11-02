package org.una.programmingIII.Assignment_Manager_Client.Util;

import javafx.event.ActionEvent;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import io.github.palexdev.materialfx.controls.MFXButton;

public class ButtonCell<T> extends TableCell<T, Boolean> {

    private final MFXButton cellButton;
    private final ImageView imageView = new ImageView();

    public ButtonCell(String buttonText, String buttonStyleClass, ButtonAction<T> action) {
        cellButton = new MFXButton(buttonText);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        cellButton.setGraphic(imageView);
        cellButton.getStyleClass().add(buttonStyleClass);

        // Define la acción del botón usando el método recibido como parámetro
        cellButton.setOnAction((ActionEvent t) -> {
            T item = (T) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
            action.execute(item);  // Ejecuta la acción con el elemento seleccionado
        });
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (!empty) {
            setGraphic(cellButton);
        } else {
            setGraphic(null);
        }
    }

    // Interfaz funcional para definir la acción
    @FunctionalInterface
    public interface ButtonAction<T> {
        void execute(T item);
    }
}


//// Para eliminar un CareerDto   /////////ejemplo de uso
//TableColumn<CareerDto, Boolean> deleteColumn = new TableColumn<>("Delete");
//deleteColumn.setCellFactory(col -> new ButtonCell<>(
//        "Delete",
//        "mfx-btn-Delete",
//career -> {
//deleteCareer((CareerDto) career);  // Define la acción específica para CareerDto
//loadCareers();
//        }
//                ));
//
//// Para navegar a un curso en CareerDto
//TableColumn<CareerDto, Boolean> courseColumn = new TableColumn<>("Course");
//courseColumn.setCellFactory(col -> new ButtonCell<>(
//        "Course",
//        "mfx-btn-Enter",
//career -> {
//        AppContext.getInstance().set("careerDto", career);
//            FlowController.getInstance().goViewInWindow("CreateCourseView");
//getStage().close();
//        }
//                ));

