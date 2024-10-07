package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UploadFileViewController extends Controller implements Initializable {
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
    private List<File> uploadedFiles = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vbxDropArea.setOnDragOver(event -> {
            if (event.getGestureSource() != vbxDropArea && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        vbxDropArea.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                List<File> files = db.getFiles();
                files.forEach(this::addFileToList);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    @FXML
    void onActionBtnCancel(ActionEvent event) {
        // Method content
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
            selectedFiles.forEach(this::addFileToList);
        }
    }

    @Override
    public void initialize() {

    }
    private void addFileToList(File file) {
        if (!uploadedFiles.contains(file)) {
            uploadedFiles.add(file);


HBox fileRow = new HBox();
fileRow.setSpacing(4);
Label fileNameLabel = new Label(file.getName());
Button deleteButton = new Button();
deleteButton.getStyleClass().add("btn-delete");
deleteButton.setPrefSize(25, 25);

deleteButton.setOnAction(event -> removeFileFromList(file, fileRow));


            fileRow.getChildren().addAll(fileNameLabel, deleteButton);
            vbxFileList.getChildren().add(fileRow);
        }
    }


    private void removeFileFromList(File file, HBox fileRow) {
        uploadedFiles.remove(file);
        vbxFileList.getChildren().remove(fileRow);
    }
}
