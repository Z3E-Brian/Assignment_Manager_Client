package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.StudentsSubmissions;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.SubmissionService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.net.URL;
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
    private Hyperlink lblDocument;
    @FXML
    private Label uploadDate, aiResponseLabel, lblRemainingTime, lblLastModification, lblShowGrade, lblComment, lblStudentName, lblAssignmentTitle, lblSubmission, lblIsQualified;
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

    @Override
    public void initialize(){
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
            } catch (Exception e) {
                submission = new StudentsSubmissions();
            }
        }

        setupViewForRole();
        initializeAssignmentData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    private void setupViewForRole() {
        boolean isStudent = !isTeacher;
        lblShowGrade.setVisible(isStudent);
        lblComment.setVisible(isStudent);
        txfGrade.setEditable(isTeacher);
        txfComment.setEditable(isTeacher);
        txfComment.setVisible(isTeacher);
        txfGrade.setVisible(isTeacher);
        lblStudentName.setVisible(isTeacher);
        btnSubmitFeedback.setVisible(isTeacher);
        btnUploadFile.setVisible(isStudent);
        btnClose.setVisible(isTeacher);

        if (isStudent) {
            vbMain.getChildren().remove(lblStudentName);
        }
    }

    private void initializeAssignmentData() {
        lblRemainingTime.setText("Remaining Time: " + assignment.getDueDate().minusDays(LocalDate.now().toEpochDay()) + " days");
        lblDocument.setText("Document");
        lblAssignmentTitle.setText(assignment.getTitle());


    }

    private void loadSubmissionData() {
        lblLastModification.setText("Last Modification: " + (submission.getCreatedAt() != null ? submission.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A"));
        lblStudentName.setText(submission.getStudentName());
        lblComment.setText(submission.getFeedback());
        lblSubmission.setText(submission.getFiles() != null ? "Submission made" : "No submission");
        lblSubmission.setStyle(submission.getFiles() != null ? "-fx-text-fill: green" : "-fx-text-fill: red");
        lblShowGrade.setText(submission.getGrade() != null ? "Grade: " + submission.getGrade() : "Not graded");
        lblShowGrade.setStyle(submission.getGrade() != null ? "-fx-text-fill: green" : "-fx-text-fill: red");
        txfGrade.setText(submission.getGrade() != null ? submission.getGrade().toString() : "");
        txfComment.setText(submission.getFeedback() != null ? submission.getFeedback() : "");
        lblSubmission.setText("Submission made");

    }

    @FXML
    private void fetchAIResponse(ActionEvent event) {
        aiResponseLabel.setText(getRandomAIResponse());
    }

    private String getRandomAIResponse() {
        List<String> responses = Arrays.asList(
                "Excellent work, keep it up.",
                "Review the instructions to improve the presentation.",
                "Good effort, but you can improve on the details.",
                "Your submission looks complete, well done."
        );
        return responses.get(new Random().nextInt(responses.size()));
    }

    @FXML
    private void submitFeedback(ActionEvent event) {
        SubmissionDto newSubmission = new SubmissionDto();
        newSubmission.setId(submission.getId());
        newSubmission.setAssignmentId(submission.getAssignmentId());
        newSubmission.setStudentId(submission.getStudentId());
        newSubmission.setCreatedAt(submission.getCreatedAt());
        newSubmission.setReviewedAt(LocalDate.now().atStartOfDay());
        newSubmission.setGrade(Double.parseDouble(txfGrade.getText()));
        newSubmission.setFeedback(txfComment.getText());

        Answer answer = submissionService.updateSubmission(newSubmission.getId(), newSubmission);
        if (!answer.getState()) {
            showError("Update Submission", answer.getMessage());
        } else {
            showInfo("Update Submission", "Submission updated successfully.");
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