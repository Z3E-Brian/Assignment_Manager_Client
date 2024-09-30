package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends Controller implements Initializable {

    @FXML
    public VBox MainMenuBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    @Override
    public void initialize() {

    }

    // Métodos de acción
    @FXML
    private void onAddAssignment() {
        System.out.println("Add Assignment");
        //FlowController.getInstance().goViewInWindow("AddAssignmentView");
    }

    @FXML
    private void onEditAssignment() {
        System.out.println("Edit Assignment");
//        AssignmentDto selected = assignmentTable.getSelectionModel().getSelectedValue();
//        if (selected != null) {
//            // Enviar la asignación seleccionada para editar
//            FlowController.getInstance().goViewInWindow("EditAssignmentView");
//        }
    }

    @FXML
    private void onDeleteAssignment() {
        System.out.println("Delete Assignment");
//        AssignmentDto selected = assignmentTable.getSelectionModel().getSelectedValue();
//        if (selected != null) {
//            assignments.remove(selected);
//        }
    }

    @FXML
    private void onMarkAsCompleted() {
        System.out.println("Mark as completed");
//        AssignmentDto selected = assignmentTable.getSelectionModel().getSelectedValue();
//        if (selected != null) {
////            selected.setDescription("Completada");
//            assignmentTable.update();
//        }
    }

}
