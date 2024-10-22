package org.una.programmingIII.Assignment_Manager_Client.Controller;


import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FileDragAndDropHandler;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UploadTaskViewController extends Controller implements Initializable {
    @FXML
    private MFXButton btnCancel;

    @FXML
    private MFXButton btnPrevious;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXButton btnUpload;

    @FXML
    private Label lblClosingDate;

    @FXML
    private Label lblOpeningDate;

    @FXML
    private Label lblTitle;

    @FXML
    private VBox vbxFileList;
    @FXML
    private VBox vbxDropArea;
    private final List<File> uploadedFiles = new ArrayList<>();
    FileDragAndDropHandler fileHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileHandler = new FileDragAndDropHandler(uploadedFiles, vbxFileList);
        fileHandler.setupDragAndDrop(vbxDropArea);
    }

    @FXML
    void onActionBtnCancel(ActionEvent event) {
        // Method content
        FlowController.getInstance().goViewInWindowModal("AddAssignmentOrFileView", getStage(), true);

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        for (File file : uploadedFiles) {
            System.out.println(file.getName());
        }
    }

    @FXML
    void onActionPrevious(ActionEvent event) {
        // Method content
    }

    @FXML
    void onAtionBtnUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());

        if (selectedFiles != null) {
            selectedFiles.forEach(file -> fileHandler.addFileToList(file));
        }
    }

    @Override
    public void initialize() {

    }

}
