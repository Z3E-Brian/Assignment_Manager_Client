package org.una.programmingIII.Assignment_Manager_Client.Controller;


import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.FileService;
import org.una.programmingIII.Assignment_Manager_Client.Service.SubmissionService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class UploadTaskViewController extends Controller {
    @FXML
    private MFXButton btnCancel, btnPrevious, btnSave, btnUpload;

    @FXML
    private Label lblClosingDate, lblOpeningDate, lblTitle;

    @FXML
    private VBox vbxFileList, vbxDropArea;

    private final List<File> uploadedFiles = new ArrayList<>();
    private FileDragAndDropHandler fileHandler;
    private UserDto userDto;
    private SubmissionDto submissionDto;
    private AssignmentDto assignmentDto;


    @FXML
    void onActionBtnCancel(ActionEvent event) {
        getStage().close();

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        if (uploadedFiles.isEmpty()) {
            showError("Save Submission", "You must upload at least one file");
            return;
        }
        saveSubmission();
        getStage().close();
    }

    @FXML
    void onActionPrevious(ActionEvent event) {
        getStage().close();
    }

    @FXML
    void onActionBtnUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            selectedFiles.forEach(file -> fileHandler.addFileToList(file));
        }
    }

    @Override
    public void initialize() {
        fileHandler = new FileDragAndDropHandler(uploadedFiles, vbxFileList);
        fileHandler.setupDragAndDrop(vbxDropArea);
        userDto =  SessionManager.getInstance().getLoginResponse().getUser();
        assignmentDto = (AssignmentDto) AppContext.getInstance().get("assignment");
        lblTitle.setText(assignmentDto.getTitle());
        lblClosingDate.setText(assignmentDto.getDueDate().toString());
    }

    private void saveSubmission() {
        try {
            submissionDto = new SubmissionDto();
            submissionDto.setAssignmentId(assignmentDto.getId());
            submissionDto.setStudentId(userDto.getId());
            submissionDto.setFiles(new ArrayList<>());
            submissionDto.setCreatedAt(LocalDateTime.now());
            Answer answer = new SubmissionService().createSubmission(submissionDto);
            if (!answer.getState()) {
                showError("Save Submission", answer.getMessage());
            } else {
                showInfo("Save Submission", "Submission saved successfully");
                submissionDto = (SubmissionDto) answer.getResult("submission");
                saveFile();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveFile() {
        try {
            for (File file : uploadedFiles) {
                FileInput fileInput = new FileInput();
                fileInput.setName(file.getName());
                fileInput.setSubmissionId(submissionDto.getId());
                Answer answer = new FileService().createFile(fileInput, file);
                if (!answer.getState()) {
                    showError("Save File", answer.getMessage());
                }
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            showError("Save File", "An error occurred saving the file");
        }
    }

    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }
}
