package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserRoleState;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.SubmissionService;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class AssignmentViewController extends Controller implements Initializable {


    private final AppContext appContext = AppContext.getInstance();
    public Label lblAperture;
    public Label lblClosure;
    public Label uploadDate;
    public MFXButton btnAddDelivery;
    public Label aiResponseLabel;
    public Label lblRemainingTime;
    public Label lblLastModification;
    public MFXTextField gradeField;
    public TextArea commentField;
    public MFXButton btnSubmitFeedback;
    public MFXButton btnUploadFile;
    public Label lblShowGrade;
    public Label lblComment;
    private AssignmentDto assignment;
    private SubmissionDto selectedSubmission;
    private final UserRoleState currentUserRole = UserRoleState.PROFESSOR; // TODO: Get from session
    private AssignmentService assignmentService;
    private SubmissionService submissionService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    @Override
    public void initialize() {
        assignment = (AssignmentDto) appContext.get("assignment");
        setupViewForRole();
        initializeAssignmentData();
        loadSubmissionData();
    }

    private void setupViewForRole() {
        // TODO: get roles and permissions from session and set the view accordingly
        boolean isTeacher = currentUserRole.equals(UserRoleState.PROFESSOR);
        gradeField.setEditable(isTeacher);
        commentField.setEditable(isTeacher);
        btnSubmitFeedback.setVisible(isTeacher);
    }

    private void initializeAssignmentData() {
        lblAperture.setText(lblAperture.getText() + " " + LocalDateTime.of(2024,12,1,0,0).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblClosure.setText(lblClosure.getText() + " " + assignment.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblRemainingTime.setText(lblRemainingTime.getText() + " " + assignment.getDueDate().minusDays(LocalDate.now().toEpochDay()) + " días");

    }

    private void loadSubmissionData() {
        SubmissionDto submission = getSubmissionForCurrentUser();
        if (submission != null) {
            gradeField.setText(submission.getGrade() != null ? submission.getGrade().toString() : "");
            commentField.setText(submission.getFeedback() != null ? submission.getFeedback() : "");
        }
    }

    private SubmissionDto getSubmissionForCurrentUser() {
        return null; // Simulated logic to get the current user's submission
    }

    @FXML
    private void fetchAIResponse(ActionEvent event) {
        aiResponseLabel.setText(getRandomAIResponse());
    }

    private String getRandomAIResponse() {
        List<String> responses = Arrays.asList(
                "Excelente trabajo, sigue así.",
                "Revisa las instrucciones para mejorar la presentación.",
                "Buen esfuerzo, pero puedes mejorar en los detalles.",
                "Tu entrega se ve completa, bien hecho."
        );
        return responses.get(new Random().nextInt(responses.size()));
    }

    @FXML
    private void submitFeedback(ActionEvent event) {
        if (selectedSubmission != null) {
            selectedSubmission.setGrade(Double.parseDouble(gradeField.getText()));
            selectedSubmission.setFeedback(commentField.getText());
            selectedSubmission.setReviewedAt(LocalDateTime.now());
            saveSubmissionFeedback(selectedSubmission);
        }
    }

    private void saveSubmissionFeedback(SubmissionDto submission) {
        submissionService.updateSubmission(submission.getId(), submission);
    }

    @FXML
    private void openComments(ActionEvent actionEvent) {
    }

    @FXML
    private void uploadFile(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInWindowModal("AddAssignmentOrFileView", this.getStage(), false);
    }

    @FXML
    private void goToUploadFile(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInWindowModal("UploadTaskView", this.getStage(), false);
    }
}