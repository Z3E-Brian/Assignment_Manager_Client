package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AddAssignmentOrFileViewController extends Controller implements Initializable {

    @FXML
    private MFXButton btnDelete, btnNew, btnSave, btnSearch, btnClear;

    @FXML
    private MFXComboBox<AssignmentType> cbxTypeAssignment;

    @FXML
    private MFXDatePicker dtpDueDateAssignment;

    @FXML
    private Tab tbpAssignment, tbpMaterial;

    @FXML
    private MFXTextField txfDescriptionAssignment, txfTitleAssignment;

    @FXML
    private VBox vbxDropAreaAssignment, vbxDropAreaMaterial, vbxFileListAssignment, vbxFileListMaterial;
    AssignmentInput assignmentInput;
    ArrayList<Node> required = new ArrayList<>();
    private final List<File> uploadedMaterials = new ArrayList<>();
    private final List<File> uploadedAssignments = new ArrayList<>();
    FileDragAndDropHandler assignmentHandler;
    FileDragAndDropHandler materialHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txfTitleAssignment.delegateSetTextFormatter(Format.getInstance().textFormat(150));
        txfDescriptionAssignment.delegateSetTextFormatter(Format.getInstance().textFormat(1000));
        cbxTypeAssignment.getItems().addAll(AssignmentType.values());
        IndicateRequired();
        assignmentHandler = new FileDragAndDropHandler(uploadedAssignments, vbxFileListAssignment);
        assignmentHandler.setupDragAndDrop(vbxDropAreaAssignment);

        materialHandler = new FileDragAndDropHandler(uploadedMaterials, vbxFileListMaterial);
        materialHandler.setupDragAndDrop(vbxDropAreaMaterial);
    }


    @Override
    public void initialize() {

    }


    @FXML
    void onActionBtnDelete(ActionEvent event) {
    if (tbpAssignment.isSelected()) {
        deleteAssignment();
    } else {
        materialHandler.clearFiles();
    }
}

private void deleteAssignment() {
    try {
        if (assignmentInput.getId() == null) {
            showError("Delete Assignment", "You must upload the assignment to be deleted");
        } else {
            Answer answer = null; // new AssignmentService().deleteAssignment(assignmentInput.getId());
            answer.setState(true);
            if (!answer.getState()) {
                showError("Delete Assignment", answer.getMessage());
            } else {
                showInfo("Delete Assignment", answer.getMessage());
                newAssignment();
            }
        }
    } catch (Exception e) {
        Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
        showError("Delete Assignment", e.getMessage());
    }
}

private void showError(String title, String message) {
    new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
}

private void showInfo(String title, String message) {
    new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
}

    @FXML
    void onActionBtnNew(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null) {
            if (tbpAssignment.isSelected()) {
                selectedFiles.forEach(assignmentHandler::addFileToList);
            } else if (tbpMaterial.isSelected()) {
                selectedFiles.forEach(materialHandler::addFileToList);
            }
        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        if (tbpAssignment.isSelected()) {
            saveAssignment();
            System.out.println("Save assignment logic here");
        } else if (tbpMaterial.isSelected()) {
            saveMaterial();
            System.out.println("Save material logic here");
        }
    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {
        SearchAssignmentViewController searchAssignmentViewController = (SearchAssignmentViewController) FlowController.getInstance().getController("SearchAssignmentView");
        FlowController.getInstance().goViewInWindowModal("SearchAssignmentView", getStage(), true);
        AssignmentInput assignmentInput1 = (AssignmentInput) searchAssignmentViewController.getResult();
        if (assignmentInput1 != null) {
            assignmentInput = assignmentInput1;
            unbindAssignment();
            bindAssignment(false);
        }
    }

    @FXML
    void onActionBtnClear(ActionEvent event) {
        if (tbpAssignment.isSelected()) {

            if (new Message().showConfirmation("Clean Assignment", getStage(), "Are you sure you want to clean the registry?")) {
                newAssignment();
            }
        } else if (tbpMaterial.isSelected()) {
            if (new Message().showConfirmation("Clean Material", getStage(), "Are you sure you want to clean the registry?")) {
                materialHandler.clearFiles();
            }
        }
    }


    private void IndicateRequired() {
        required.clear();
        required.addAll(Arrays.asList(txfTitleAssignment, txfDescriptionAssignment, dtpDueDateAssignment, cbxTypeAssignment));
    }

    private String validateRequired() {
        StringBuilder invalid = new StringBuilder();
        for (Node node : required) {
            String floatingText = getFloatingText(node);
            if (floatingText != null) {
                if (!invalid.isEmpty()) {
                    invalid.append(", ");
                }
                invalid.append(floatingText);
            }
        }
        return invalid.isEmpty() ? "" : "Fields required or with formatting problems [" + invalid + "].";
    }

    private String getFloatingText(Node node) {
        String floatingText = null;
        if (node instanceof MFXTextField && isEmpty(((MFXTextField) node).getText())) {
            floatingText = ((MFXTextField) node).getFloatingText();
        } else if (node instanceof MFXComboBox && ((MFXComboBox<?>) node).getValue() == null) {
            floatingText = ((MFXComboBox<?>) node).getFloatingText();
        } else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
            floatingText = ((MFXDatePicker) node).getFloatingText();
        }
        return floatingText;
    }

    private boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    private void newAssignment() {
        assignmentInput = new AssignmentInput();
        unbindAssignment();
        bindAssignment(true);
        txfTitleAssignment.requestFocus();
        assignmentHandler.clearFiles();
    }

    private void bindAssignment(Boolean newAssignment) {
        txfTitleAssignment.textProperty().bindBidirectional(assignmentInput.title);
        txfDescriptionAssignment.textProperty().bindBidirectional(assignmentInput.description);
        dtpDueDateAssignment.valueProperty().bindBidirectional(assignmentInput.dueDate);
        cbxTypeAssignment.setValue(assignmentInput.type);

    }

    private void unbindAssignment() {
        txfTitleAssignment.textProperty().bindBidirectional(assignmentInput.title);
        txfDescriptionAssignment.textProperty().bindBidirectional(assignmentInput.description);
        dtpDueDateAssignment.valueProperty().bindBidirectional(assignmentInput.dueDate);
    }

private void saveAssignment() {
    try {
        String validationMessage = validateRequired();
        if (!validationMessage.isEmpty()) {
            showError("Save Assignment", validationMessage);
            return;
        }
        Answer answer = null; // new AssignmentService().createAssignment(assignmentInput);
        answer.setState(true);
        if (!answer.getState()) {
            showError("Save Assignment", answer.getMessage());
        } else {
            showInfo("Save Assignment", answer.getMessage());
            newAssignment();
        }
    } catch (Exception e) {
        Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
        showError("Save Assignment", "An error occurred saving the assignment");
    }
}
    private void saveMaterial() {
        try {
            if (!validateRequired().isEmpty()) {

                Answer answer = null;// new AssignmentService().addMaterial(assignmentInput);
                answer.setState(true);
                if (!answer.getState()) {
                    new Message().showModal(Alert.AlertType.ERROR, "Save Material", getStage(), answer.getMessage());
                } else {
                    new Message().showModal(Alert.AlertType.INFORMATION, "Save Material", getStage(), answer.getMessage());
                    newAssignment();
                }

            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Save Material", getStage(), validateRequired());
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            new Message().showModal(Alert.AlertType.ERROR, "Save Material", getStage(), "An error occurred saving the material");
        }
    }

}

