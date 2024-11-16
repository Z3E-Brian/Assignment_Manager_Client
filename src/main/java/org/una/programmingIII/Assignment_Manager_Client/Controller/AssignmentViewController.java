package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.*;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.StudentsSubmissions;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.FileService;
import org.una.programmingIII.Assignment_Manager_Client.Service.SubmissionService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class AssignmentViewController extends Controller implements Initializable {

    @FXML
    private VBox vbMain;
    @FXML
    private Label lblDocument, uploadDate, aiResponseLabel, lblRemainingTime, lblLastModification, lblShowGrade, lblComment, lblStudentName, lblAssignmentTitle, lblSubmission, lblIsQualified;
    @FXML
    private MFXButton btnSubmitFeedback, btnUploadFile, btnClose;
    @FXML
    private MFXTextField txfGrade;
    @FXML
    private TextArea txfComment;

    private boolean isTeacher;
    private AssignmentDto assignment;
    private StudentsSubmissions submission;
    private final SubmissionService submissionService = new SubmissionService();
    private final UserDto userDto = SessionManager.getInstance().getLoginResponse().getUser();

    @Override
    public void initialize() {
        clearAll();
        isTeacher = SessionManager.getInstance().getLoginResponse().getUser().getPermissions().stream()
                .anyMatch(permission -> permission.getName() == PermissionType.CREATE_ASSIGNMENTS);
        assignment = (AssignmentDto) AppContext.getInstance().get("assignment");

        if (isTeacher) {
            submission = (StudentsSubmissions) AppContext.getInstance().get("submission");
            loadSubmissionData();
        } else {
            try {
                submission = submissionService.getSubmissionByAssignmentIdAndStudentId(assignment.getId(), SessionManager.getInstance().getLoginResponse().getUser().getId());
                loadSubmissionData();
                if (submission.getStudentId() != null) {
                    loadStudent(submission.getStudentId());
                }

                if (submission.getGrade() != null) {
                    lblIsQualified.setText("Qualified");
                }

            } catch (Exception e) {
                submission = new StudentsSubmissions();
            }
        }

        setupViewForRole();
        initializeAssignmentData();
    }

    private void loadStudent(Long id) {
        try {
            UserDto userDto = new UserService().getUserById(id);
            lblStudentName.setText(userDto.getFullName());
        } catch (Exception e) {
            showError("Error", "Error to get the student");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    @FXML
    void onMouseClickedDocument(MouseEvent event) {
        downloadFile(lblDocument.getText());
    }

    private void setupViewForRole() {
        boolean isStudent = !isTeacher;
        lblShowGrade.setVisible(isStudent);
        lblComment.setVisible(isStudent);
        txfGrade.setEditable(isTeacher);
        txfComment.setEditable(isTeacher);
        txfComment.setVisible(isTeacher);
        txfGrade.setVisible(isTeacher);
        btnSubmitFeedback.setVisible(userDto.getPermissions().stream().anyMatch(permission -> permission.getName() == PermissionType.SUBMIT_FEEDBACK));
        btnUploadFile.setVisible(userDto.getPermissions().stream().anyMatch(permission -> permission.getName() == PermissionType.SUBMIT_ASSIGNMENTS));
        btnClose.setVisible(isTeacher);

    }

    private void initializeAssignmentData() {
        LocalDate dueDate = assignment.getDueDate();
        LocalDate actualDate = LocalDate.now();
        String timeForSubmission = String.valueOf(dueDate.toEpochDay() - actualDate.toEpochDay());
        lblRemainingTime.setText(timeForSubmission + " days remaining");

        lblAssignmentTitle.setText(assignment.getTitle());
    }

    private void loadSubmissionData() {
        if (submission != null) {
            lblLastModification.setText((submission.getCreatedAt() != null ? submission.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A"));
            lblStudentName.setText(submission.getStudentName());
            lblComment.setText(submission.getFeedback());
            lblSubmission.setText(submission.getFiles() != null ? "Submission made" : "No submission");
            lblSubmission.setStyle(submission.getFiles() != null ? "-fx-text-fill: green" : "-fx-text-fill: red");
            lblShowGrade.setText(submission.getGrade() != null ? String.valueOf(submission.getGrade()) : "Not graded");
            lblShowGrade.setStyle(submission.getGrade() != null ? "-fx-text-fill: green" : "-fx-text-fill: red");
            txfGrade.setText(submission.getGrade() != null ? submission.getGrade().toString() : "");
            txfComment.setText(submission.getFeedback() != null ? submission.getFeedback() : "");
        } else {
            submission = new StudentsSubmissions();
            lblLastModification.setText("N/A");
            lblStudentName.setText("N/A");
            lblComment.setText("N/A");
            lblSubmission.setText("No submission");
            lblSubmission.setStyle("-fx-text-fill: red");
            lblShowGrade.setText("Not graded");
            lblShowGrade.setStyle("-fx-text-fill: red");
            txfGrade.setText("");
            txfComment.setText("");
            lblSubmission.setText("Submission made");
        }

        FileDto fileDto = assignment.getFiles().getFirst();
        if (fileDto != null) {
            lblDocument.setText(fileDto.getName());
        } else {
            lblDocument.setText("No file uploaded");
        }
    }

    private void downloadFile(String labelText) {
        List<FileDto> files = assignment.getFiles();
        FileDto fileDto = files.stream().filter(file -> file.getName().equals(labelText)).findFirst().orElse(null);
        if (fileDto != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(fileDto.getName());
            fileChooser.setTitle("Save File");
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                try {
                    new FileService().downloadFileInChunks(fileDto.getId(), Paths.get(file.getAbsolutePath()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendEmailToStudent(SubmissionDto submissionDto) {
        EmailDto emailDto = new EmailDto();
        emailDto.setSubject(assignment.getTitle());
        try {
            UserDto userDto = new UserService().getUserById(submissionDto.getStudentId());
            emailDto.setEmail(userDto.getEmail());
            emailDto.setMessage("Your submission has been reviewed again, your new grade is: " + submissionDto.getGrade());
            new AssignmentService().sendEmail(emailDto);
        } catch (Exception e) {
            showError("Error", "Error to get the student");
        }

    }

    @FXML
    private void submitFeedback(ActionEvent event) {
        if (userDto.getPermissions().stream().noneMatch(permission -> permission.getName() == PermissionType.SUBMIT_FEEDBACK)) {
            showError("Error", "You don't have permission to submit feedback.");
            return;
        }
        SubmissionDto newSubmission = new SubmissionDto();
        newSubmission.setId(submission.getId());
        newSubmission.setAssignmentId(submission.getAssignmentId());
        newSubmission.setStudentId(submission.getStudentId());
        newSubmission.setCreatedAt(submission.getCreatedAt());
        newSubmission.setReviewedAt(LocalDate.now().atStartOfDay());
        newSubmission.setGrade(Double.parseDouble(txfGrade.getText()));
        newSubmission.setFeedback(txfComment.getText());
        newSubmission.setReviewedById(SessionManager.getInstance().getLoginResponse().getUser().getId());

        Answer answer = submissionService.updateSubmission(newSubmission.getId(), newSubmission);

        if (!answer.getState()) {
            showError("Update Submission", answer.getMessage());
        } else {
            showInfo("Update Submission", "Submission updated successfully.");
            sendEmailToStudent(newSubmission);
        }
    }

    @FXML
    private void uploadFile(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInWindowModal("AddAssignmentOrFileView", this.getStage(), false);
    }

    @FXML
    private void goToUploadFile(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInWindowModal("UploadTaskView", this.getStage(), false);
        initialize();
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        this.getStage().close();
    }

    private void clearAll() {
        lblRemainingTime.setText("");
        lblLastModification.setText("");
        lblStudentName.setText("");
        lblAssignmentTitle.setText("");
        lblSubmission.setText("");
        lblShowGrade.setText("");
        lblIsQualified.setText("");
        txfGrade.setText("");
        txfComment.setText("");
    }

    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }
}