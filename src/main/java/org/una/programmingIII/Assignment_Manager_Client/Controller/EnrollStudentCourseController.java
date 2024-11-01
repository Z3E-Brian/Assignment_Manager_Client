package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;

public class EnrollStudentCourseController extends Controller {

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
    private TableColumn<CourseDto, Boolean> tbcEnroll;

    @FXML
    private TableColumn<CourseDto, String> tbcEnrolledCourses;

    @FXML
    private TableColumn<CourseDto, Boolean> tbcUnenroll;

    @FXML
    private TableColumn<CourseDto, Boolean> clmAvailableCourses;

    @FXML
    private TableView<CourseDto> tbvCourses;

    private UserService userService;
    private UserDto studentDto;
    private CareerDto careerDto;
    private CourseService courseService;

    @Override
    public void initialize() {
        userService = new UserService();
        studentDto = new UserDto();
        careerDto = new CareerDto();
        courseService = new CourseService();
        loadInitialData();
        System.out.println("EnrollStudentCourseController");
    }


    @FXML
    void OnMousePressedTbvCourses(MouseEvent event) {

    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {

    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {

    }

    private void loadCareerInfo() {
        try {
            Long careerId = SessionManager.getInstance().getLoginResponse().getUser().getCareerId();
            Answer answer = new CareerService().getById(careerId);
            if (answer.getState()) {
                careerDto = (CareerDto) answer.getResult("careerDto");
                lblCareerName.setText(careerDto.getName());
            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Load Career", getStage(), "Can't load the career label correctly");
            }
        } catch (
                Exception e) {
            new Message().showModal(Alert.AlertType.WARNING, "Error de conexion", getStage(), "Debe selecionar una de las universidades en la tabla para poder eliminarla.");

        }


    }

    private void loadInitialData() {
        studentDto = SessionManager.getInstance().getLoginResponse().getUser();
        lblStudentName.setText(studentDto.getName());
        loadCareerInfo();
    }


}