package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.*;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.StudentsSubmissions;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.SubmissionService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AssignmentSubmissionsViewController extends Controller implements Initializable {

    @FXML
    public Label lblAssignmentTitle;
    @FXML
    private TableView<StudentsSubmissions> submissionsTable;

    @FXML
    private TableColumn<StudentsSubmissions, String> studentNameColumn;

    @FXML
    private TableColumn<StudentsSubmissions, Long> gradeColumn;

    @FXML
    private TableColumn<StudentsSubmissions, List<FileDto>> uploadedFileColumn;

    @FXML
    private TableColumn<StudentsSubmissions, MFXButton> detailsColumn;

    private final SubmissionService submissionService = new SubmissionService();
    private final UserService userService = new UserService();
    private final AssignmentService assignmentService = new AssignmentService();
    private final UserDto userSession = SessionManager.getInstance().getLoginResponse().getUser();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    private void configureTable() {
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        //TODO: necesito usar el fileservice para poner un label y poder desacargar el archivo correspondiente
        uploadedFileColumn.setCellValueFactory(new PropertyValueFactory<>("files"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        submissionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void loadSubmissions() throws Exception {
        if (userSession.getPermissions().stream().noneMatch(permission -> permission.getName().equals(PermissionType.VIEW_ASSIGNMENTS))){
            throw new Exception("You don't have permission to view assignments");
        }
        AssignmentDto assignment = (AssignmentDto) AppContext.getInstance().get("assignment");
        lblAssignmentTitle.setText(assignment.getTitle());
        List<SubmissionDto> submissions = submissionService.getSubmissionByAssignmentId(assignment.getId());

        if (submissions == null || submissions.isEmpty()) {
            throw new Exception("No submissions found");
        }

        List<StudentsSubmissions> studentsSubmissionsList = new ArrayList<>();
        for (SubmissionDto submission : submissions) {
            UserDto user = userService.getUserById(submission.getStudentId());
            StudentsSubmissions studentsSubmissions = getStudentsSubmissions(submission, user, assignment);
            studentsSubmissionsList.add(studentsSubmissions);
        }

        submissionsTable.getItems().setAll(studentsSubmissionsList);
    }

    private static StudentsSubmissions getStudentsSubmissions(SubmissionDto submission, UserDto user, AssignmentDto assignment) {
        String studentName = user.getName() + " " + user.getLastName() + " " + user.getSecondLastName();
        String assignmentTitle = assignment.getTitle();
        List<FileInput> files = submission.getFiles();
        String feedback = submission.getFeedback();
        Integer grade = submission.getGrade() != null ? submission.getGrade().intValue() : null;
        return new StudentsSubmissions(submission.getId(),
                studentName,
                files,
                assignmentTitle,
                submission.getCreatedAt(),
                feedback,
                grade,
                submission.getReviewedAt(),
                submission.getAssignmentId(),
                submission.getReviewedById(),
                submission.getStudentId());
    }

    private void openDetailsModal(StudentsSubmissions submission) {
        AppContext.getInstance().delete("submission");
        AppContext.getInstance().set("submission", submission);
        FlowController.getInstance().goViewInWindowModal("AssignmentView", getStage(), false);
        FlowController.getInstance().getController("AssignmentView").initialize();

    }

    @FXML
    public void updateTable() {
        initialize();
    }


    @Override
    public void initialize() {
        configureTable();

        detailsColumn.setCellFactory(col -> new TableCell<>() {
            private final MFXButton detailsButton = new MFXButton("View");

            @Override
            protected void updateItem(MFXButton item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                    detailsButton.setOnAction(event -> openDetailsModal(getTableView().getItems().get(getIndex())));
                }
            }
        });
        try {
            loadSubmissions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
