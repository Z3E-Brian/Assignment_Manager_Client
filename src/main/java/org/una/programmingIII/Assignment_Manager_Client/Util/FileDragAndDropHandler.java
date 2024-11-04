package org.una.programmingIII.Assignment_Manager_Client.Util;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class FileDragAndDropHandler {

    private final List<File> uploadedFiles;
    private final VBox vbxFileList;

    public FileDragAndDropHandler(List<File> uploadedFiles, VBox vbxFileList) {
        this.uploadedFiles = uploadedFiles;
        this.vbxFileList = vbxFileList;
    }

    public void setupDragAndDrop(VBox vbxDropArea) {
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


    public void addFileToList(File file) {
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
    public void clearFiles(){
        uploadedFiles.clear();
        vbxFileList.getChildren().clear();
    }
}
