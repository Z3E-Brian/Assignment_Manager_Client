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
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.Format;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AddAssignmentOrFileViewController extends Controller implements Initializable {

    @FXML
    private MFXButton btnDelete, btnNew, btnSave, btnSearch;

    @FXML
    private MFXComboBox<AssignmentType> cbxTypeAssignment;

    @FXML
    private MFXDatePicker dtpDueDateAssignment;

    @FXML
    private Tab tbpAssignment, tbpMaterial;

    @FXML
    private MFXTextField txfDescriptionAssignment, txfIdAssignment, txfTitleAssignment;

    @FXML
    private VBox vbxDropAreaAssignment, vbxDropAreaMaterial, vbxFileListAssignment, vbxFileListMaterial;
    AssignmentInput assignmentInput;
    ArrayList<Node> required = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txfIdAssignment.delegateSetTextFormatter(Format.getInstance().integerFormat());
        txfTitleAssignment.delegateSetTextFormatter(Format.getInstance().textFormat(150));
        txfDescriptionAssignment.delegateSetTextFormatter(Format.getInstance().textFormat(1000));
        cbxTypeAssignment.getItems().addAll(AssignmentType.values());
        IndicateRequired();
    }

    @Override
    public void initialize() {

    }


    @FXML
    void onActionBtnDelete(ActionEvent event) {
        try {
            if (assignmentInput.getId() == null) {
                new Message().showModal(Alert.AlertType.ERROR, "Delete Assignment", getStage(), "You must upload the assignment to be deleted");
            } else {
                Answer answer = null;// new AssignmentService().deleteAssignment(assignmentInput.getId());
                answer.setState(true);
                if (!answer.getState()) {
                    new Message().showModal(Alert.AlertType.ERROR, "Delete Assignment", getStage(), answer.getMessage());
                } else {
                    new Message().showModal(Alert.AlertType.INFORMATION, "Delete Assignment", getStage(), answer.getMessage());
                    newAssignment();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            new Message().showModal(Alert.AlertType.ERROR, "Delete Assignment", getStage(), e.getMessage());
        }
    }

    @FXML
    void onActionBtnEdit(ActionEvent event) {

    }

    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (new Message().showConfirmation("Clean Assignment", getStage(), "Are you sure you want to clean the registry?")) {
            newAssignment();
        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) {

    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {

    }

    @FXML
    void onSelectionChangedAssignment(ActionEvent event) {

    }

    @FXML
    void onSelectionChangedMaterial(ActionEvent event) {

    }

    private void IndicateRequired() {
        required.clear();
        required.addAll(Arrays.asList(txfIdAssignment, txfTitleAssignment, txfDescriptionAssignment, dtpDueDateAssignment, cbxTypeAssignment));
    }

    private boolean validateRequired() {
        for (Node node : required) {
            if (node instanceof MFXTextField) {
                MFXTextField textField = (MFXTextField) node;
                if (textField.getText().isEmpty()) {
                    return false;
                }
            } else if (node instanceof MFXComboBox) {
                MFXComboBox<?> comboBox = (MFXComboBox<?>) node;
                if (comboBox.getValue() == null) {
                    return false;
                }
            } else if (node instanceof MFXDatePicker) {
                MFXDatePicker datePicker = (MFXDatePicker) node;
                if (datePicker.getValue() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void newAssignment() {
        assignmentInput = new AssignmentInput();
        unbindAssignment();
        bindAssignment(true);
        txfIdAssignment.clear();
        txfIdAssignment.requestFocus();
    }

    private void bindAssignment(Boolean newAssignment) {
        if (!newAssignment) {
            txfIdAssignment.textProperty().bind(assignmentInput.id);
        }
        txfTitleAssignment.textProperty().bindBidirectional(assignmentInput.title);
        txfDescriptionAssignment.textProperty().bindBidirectional(assignmentInput.description);
        dtpDueDateAssignment.valueProperty().bindBidirectional(assignmentInput.dueDate);
        cbxTypeAssignment.setValue(assignmentInput.type);

    }

    private void unbindAssignment() {
        txfIdAssignment.textProperty().unbind();
        txfTitleAssignment.textProperty().bindBidirectional(assignmentInput.title);
        txfDescriptionAssignment.textProperty().bindBidirectional(assignmentInput.description);
        dtpDueDateAssignment.valueProperty().bindBidirectional(assignmentInput.dueDate);
    }
}

