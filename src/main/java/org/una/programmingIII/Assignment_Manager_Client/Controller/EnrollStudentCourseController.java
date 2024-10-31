package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.fxml.FXML;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EnrollStudentCourseController extends Controller {
    @FXML
    private TableColumn<?, ?> clmAvailableCourses;

    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvSearch;

    @FXML
    private Label lblCareerName;

    @FXML
    private Label lblCourses;

    @FXML
    private Label lblStudentName;

    @FXML
    private TableColumn<?, ?> tbcEnroll;

    @FXML
    private TableColumn<?, ?> tbcEnrolledCourses;

    @FXML
    private TableColumn<?, ?> tbcUnenroll;

    @FXML
    private TableView<?> tbvCourses;

    @FXML
    void OnMousePressedTbvCourses(MouseEvent event) {

    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {

    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {

    }

    @Override
    public void initialize() {
        System.out.println("EnrollStudentCourseController");
    }
}
