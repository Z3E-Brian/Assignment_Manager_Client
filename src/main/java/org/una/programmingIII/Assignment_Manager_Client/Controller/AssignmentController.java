package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import org.una.programmingIII.Assignment_Manager_Client.Model.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;

public class AssignmentController extends Controller /*implements initializable*/ {

    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dueDateLabel;

    private final AssignmentService assignmentService = new AssignmentService();

    public void loadAssignment(Long id) {
//        try {
//            AssignmentDto assignment = assignmentService.getAssignmentById(id);
//            titleLabel.setText(assignment.getTitle());
//            descriptionLabel.setText(assignment.getDescription());
//            dueDateLabel.setText(assignment.getDueDate().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void initialize() {

    }


}
