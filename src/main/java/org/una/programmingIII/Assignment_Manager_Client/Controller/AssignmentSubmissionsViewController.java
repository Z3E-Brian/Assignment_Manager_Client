package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FileDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.StudentsSubmissions;
import org.una.programmingIII.Assignment_Manager_Client.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.SubmissionService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuración de columnas
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


    private void configureTable() {
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        uploadedFileColumn.setCellValueFactory(new PropertyValueFactory<>("files"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    private void loadSubmissions() throws Exception {
        AssignmentDto assignment = (AssignmentDto) AppContext.getInstance().get("assignment");

        lblAssignmentTitle.setText(assignment.getTitle());

        List<SubmissionDto> submissions = submissionService.getSubmissionByAssignmentId(assignment.getId());

        if(submissions == null || submissions.isEmpty()) {
            System.out.println("No hay nada aun");
            return;
        }

        List<StudentsSubmissions> studentsSubmissionsList = new ArrayList<>();

        for (SubmissionDto submission : submissions) {
            String studentName = userService.getUserById(submission.getStudentId()).getName();
            String assignmentTitle = assignment.getTitle();
            List<FileInput> files = submission.getFiles();
            String feedback = submission.getFeedback();
            Integer grade = submission.getGrade() != null ? submission.getGrade().intValue() : null;
            StudentsSubmissions studentsSubmissions = new StudentsSubmissions(submission.getId(), studentName, files, assignmentTitle, submission.getCreatedAt(), feedback, grade, submission.getReviewedAt(), submission.getAssignmentId(), submission.getReviewedById(), submission.getStudentId());
            studentsSubmissionsList.add(studentsSubmissions);
        }

        submissionsTable.getItems().setAll(studentsSubmissionsList);









//        // 4. Crear lista de StudentsSubmissions para la tabla
//        List<StudentsSubmissions> studentsSubmissionsList = submissions.stream().map(submission -> {
//            try {
//                /***************************************************/
//                String studentName = userService.getUserById(submission.getStudentId()).getName();
//
//                // Obtener el título de la asignación (aunque ya está en assignment)
//                String assignmentTitle = assignment.getTitle();
//
//                // Mapeo del archivo (puedes ajustar según la estructura de tu FileDto y FileInput)
//
//                // Crear la instancia de StudentsSubmissions
//                return new StudentsSubmissions(
//                        submission.getId(),
//                        studentName,
//                        submission.getFiles(),
//                        assignmentTitle,
//                        submission.getCreatedAt(),
//                        submission.getFeedback(),
//                        submission.getGrade() != null ? submission.getGrade().intValue() : null,
//                        submission.getReviewedAt(),
//                        submission.getAssignmentId(),
//                        submission.getReviewedById(),
//                        submission.getStudentId()
//                );
//            } catch (Exception e) {
//                throw new RuntimeException("Error loading submission data", e);
//            }
//        }).toList();
//
//        // 5. Configurar la tabla con la lista de StudentsSubmissions
//        submissionsTable.getItems().setAll(studentsSubmissionsList);
//
//        // 6. Configurar el botón de detalles para cada fila de la tabla
//        detailsColumn.setCellFactory(col -> new TableCell<>() {
//            private final MFXButton detailsButton = new MFXButton("Ver Detalles");
//
//            @Override
//            protected void updateItem(MFXButton item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(detailsButton);
//                    StudentsSubmissions currentSubmission = getTableView().getItems().get(getIndex());
//                    detailsButton.setOnAction(event -> openDetailsModal(currentSubmission));
//                }
//            }
//        });
    }

    // Método para abrir la vista de detalles con la información de StudentsSubmissions
    private void openDetailsModal(StudentsSubmissions submission) {
        AppContext.getInstance().set("submission", submission);
        FlowController.getInstance().goViewInWindowModal("SubmissionDetailsView", getStage(), false);
    }


    private void openDetailsModal(SubmissionDto submission) {
        AppContext.getInstance().set("submission", submission);
        FlowController.getInstance().goViewInWindowModal("SubmissionDetailsView",getStage(),false);
    }


    @Override
    public void initialize() {

    }
}
