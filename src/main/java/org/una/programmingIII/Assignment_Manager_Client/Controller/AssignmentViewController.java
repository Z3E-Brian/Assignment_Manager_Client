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
    public VBox vbMain;
    @FXML
    public Hyperlink lblDocument;
    @FXML
    private Label uploadDate, aiResponseLabel, lblRemainingTime, lblLastModification, lblShowGrade, lblComment, lblStudentName, lblAssignmentTitle, lblSubmission, lblIsQualified;
    @FXML
    private MFXButton btnAddDelivery, btnSubmitFeedback, btnUploadFile,btnClose;
    @FXML
    private MFXTextField txfGrade;
    @FXML
    private TextArea txfComment;

    boolean isTeacher;

    private AssignmentDto assignment;
    private StudentsSubmissions submission;
    private final SubmissionService submissionService = new SubmissionService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initialize();
    }

    private void setupViewForRole() {
        lblShowGrade.setVisible(!isTeacher);
        lblComment.setVisible(!isTeacher);
        txfGrade.setEditable(isTeacher);
        txfComment.setEditable(isTeacher);
        txfComment.setVisible(isTeacher);
        txfGrade.setVisible(isTeacher);
        lblStudentName.setVisible(isTeacher);
        btnSubmitFeedback.setVisible(isTeacher);
        btnUploadFile.setVisible(!isTeacher);
        btnClose.setVisible(isTeacher);

        if (!isTeacher) {
            vbMain.getChildren().remove(lblStudentName);
        }
    }

    private void initializeAssignmentData() {
        lblRemainingTime.setText("Remaining Time: " + assignment.getDueDate().minusDays(LocalDate.now().toEpochDay()) + " days");
        lblLastModification.setText("Last Modification: " + submission.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblStudentName.setText(submission.getStudentName());
        //TODO: Cambiar para q muestre el archivo y permita descargarlo
        lblDocument.setText("Document");

        lblComment.setText(submission.getFeedback());
        lblAssignmentTitle.setText(assignment.getTitle());
        //Cambiar para q muestre el archivo
        if (submission.getFiles() != null) {
            lblSubmission.setText("Submission made");
            lblSubmission.setStyle("-fx-text-fill: green");
        } else {
            lblSubmission.setText("No submission");
            lblSubmission.setStyle("-fx-text-fill: red");
        }
        if (submission.getGrade() != null) {
            lblShowGrade.setText("Grade: " + submission.getGrade());
            lblShowGrade.setStyle("-fx-text-fill: green");
        } else {
            lblShowGrade.setText("Not graded");
            lblShowGrade.setStyle("-fx-text-fill: red");
        }
    }

    private void loadSubmissionData() {
        if (submission != null) {
            txfGrade.setText(submission.getGrade() != null ? submission.getGrade().toString() : "");
            txfComment.setText(submission.getFeedback() != null ? submission.getFeedback() : "");
        }
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
        newSubmission.setId(this.submission.getId());
        newSubmission.setAssignmentId(this.submission.getAssignmentId());
        newSubmission.setStudentId(this.submission.getStudentId());
        newSubmission.setCreatedAt(this.submission.getCreatedAt());
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
    }

    @Override
    public void initialize() {
        clearAll();
        isTeacher = SessionManager.getInstance().getLoginResponse().getUser().getPermissions().stream()
                .anyMatch(permission -> permission.getName() == PermissionType.CREATE_ASSIGNMENTS);
        assignment = (AssignmentDto) AppContext.getInstance().get("assignment");

        if (isTeacher){
            submission = (StudentsSubmissions) AppContext.getInstance().get("submission");
        } else {
            try {
                submission = submissionService.getSubmissionByAssignmentIdAndStudentId(assignment.getId(), SessionManager.getInstance().getLoginResponse().getUser().getId());
            } catch (Exception e) {
                submission = null;
            }
        }

        setupViewForRole();
        initializeAssignmentData();
        loadSubmissionData();


    }

    private void clearAll(){
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

    @FXML
    public void close(ActionEvent actionEvent) {
        this.getStage().close();
    }

    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}