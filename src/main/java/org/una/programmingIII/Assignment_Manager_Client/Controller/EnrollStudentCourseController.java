package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.una.programmingIII.Assignment_Manager_Client.Dto.*;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Set;

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
    private TableColumn<CourseDto, String> tbcAvailableCourses;

    @FXML
    private TableView<CourseDto> tbvAvailableCourses;

    @FXML
    private TableView<CourseDto> tbvEnrollCourses;

    private UserDto studentDto;
    private CareerDto careerDto;
    private CourseService courseService;
    private boolean isProfessorSession;
    Long professorId;

    @Override
    public void initialize() {
        studentDto = new UserDto();
        careerDto = new CareerDto();
        courseService = new CourseService();
        isProfessorSession = false;

        tbcEnrolledCourses.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcAvailableCourses.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcUnenroll.setCellValueFactory((TableColumn.CellDataFeatures<CourseDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcUnenroll.setCellFactory((TableColumn<CourseDto, Boolean> p) -> new ButtonCellUnEnrollCourse());
        tbcEnroll.setCellValueFactory((TableColumn.CellDataFeatures<CourseDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcEnroll.setCellFactory((TableColumn<CourseDto, Boolean> p) -> new ButtonCellEnrollCourse());
        tbvAvailableCourses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tbvEnrollCourses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        loadInitialData();
    }


    @FXML
    void OnMousePressedTbvCourses(MouseEvent event) {

    }

    @FXML
    void OnMousePressedTbvEnrollCourses(MouseEvent event) {

    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        if (isProfessorSession) {
            FlowController.getInstance().deleteAndLoadView("SelectStudentToEnrollView");
        } else {
            FlowController.getInstance().goMain();
        }
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {

    }

    private void loadCareerInfo() {
        try {
            Long careerId = studentDto.getCareerId();
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

    private void loadCoursesStudentLogin() {
        loadEnrolledCoursesStudentSession();
        loadAvailableCoursesStudentSession();
    }

    private void loadCoursesProfessorLogin() {
        loadAvailableCoursesProfessorSession();
        loadEnrolledCoursesProfessorSession();
    }

    private void loadCourses() {
        clearTables();
        if (isProfessorSession) {
            loadCoursesProfessorLogin();
        } else {
            loadCoursesStudentLogin();
        }
    }

    private void clearTables() {
        tbvAvailableCourses.getItems().clear();
        tbvEnrollCourses.getItems().clear();

        tbcEnroll.setCellFactory(null);
        tbcUnenroll.setCellFactory(null);

        tbcUnenroll.setCellFactory((TableColumn<CourseDto, Boolean> p) -> new ButtonCellUnEnrollCourse());
        tbcEnroll.setCellFactory((TableColumn<CourseDto, Boolean> p) -> new ButtonCellEnrollCourse());
    }

    private void loadEnrolledCoursesStudentSession() {
        try {
            List<CourseDto> coursesStudentDto = new CourseService().getEnrolledCoursesByStudentId(studentDto.getId());
            ObservableList<CourseDto> courseDtos = FXCollections.observableArrayList(coursesStudentDto);
            tbvEnrollCourses.getItems().clear();
            tbvEnrollCourses.setItems(courseDtos);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Connection Error", getStage(), "Can't retrieve courses");
        }
    }

    private void loadAvailableCoursesStudentSession() {
        try {
            List<CourseDto> coursesStudentDto = new CourseService().getAvailableCoursesForAStudentInCareer(careerDto.getId(), studentDto.getId());
            ObservableList<CourseDto> courseDtos = FXCollections.observableArrayList(coursesStudentDto);
            tbvAvailableCourses.getItems().clear();
            tbvAvailableCourses.setItems(courseDtos);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Connection Error", getStage(), "Can't retrieve courses");
        }
    }

    private void loadEnrolledCoursesProfessorSession() {
        try {
            List<CourseDto> coursesStudentDto = new CourseService().findCoursesEnrolledByStudentIdAAndProfessorIs(professorId, studentDto.getId());
            ObservableList<CourseDto> courseDtos = FXCollections.observableArrayList(coursesStudentDto);
            tbvEnrollCourses.getItems().clear();
            tbvEnrollCourses.setItems(courseDtos);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Connection Error", getStage(), "Can't retrieve courses");
        }
    }

    private void loadAvailableCoursesProfessorSession() {
        try {
            List<CourseDto> coursesStudentDto = new CourseService().findAvailableCoursesByCareerIdUserIdAndProfessorId(professorId, studentDto.getId());
            ObservableList<CourseDto> courseDtos = FXCollections.observableArrayList(coursesStudentDto);
            tbvAvailableCourses.getItems().clear();
            tbvAvailableCourses.setItems(courseDtos);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Connection Error", getStage(), "Can't retrieve courses");
        }
    }

    private void loadInitialData() {
        studentDto = (UserDto) AppContext.getInstance().get("studentDto");
        lblStudentName.setText(studentDto.getName());
        checkProfessorSession();
        loadCareerInfo();
        loadCourses();
    }

    private void checkProfessorSession() {
        Set<PermissionDto> loginUserPermissions = SessionManager.getInstance()
                .getLoginResponse()
                .getUser()
                .getPermissions();

        boolean hasProfessorPermission = loginUserPermissions.stream()
                .anyMatch(permission -> PermissionType.PROFESSOR.equals(permission.getName()));

        if (hasProfessorPermission) {
            isProfessorSession = true;
            this.professorId = SessionManager.getInstance().getLoginResponse().getUser().getId();
        }
    }

    private void enrollStudentInCourse(Long courseId) {
        try {
            Answer answer = courseService.enrollStudentInCourse(studentDto.getId(), courseId);
            if (answer.getState()) {
                new Message().showModal(Alert.AlertType.INFORMATION, "Enroll course", getStage(), "Enrolled sucessfully");
                loadCourses();
            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Enroll course", getStage(), "Something went wrong with course enrollment");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void unEnrollStudentInCourse(Long courseId) {
        try {
            Answer answer = courseService.unenrollStudentFromCourse(studentDto.getId(), courseId);
            if (answer.getState()) {
                new Message().showModal(Alert.AlertType.INFORMATION, "Enroll course", getStage(), "Unenrolled sucessfully");
                loadCourses();
            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Uneroll course", getStage(), "Something went wrong with course unEnrollment");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class ButtonCellEnrollCourse extends TableCell<CourseDto, Boolean> {

        final MFXButton cellButton = new MFXButton("Course");
        ImageView imageView = new ImageView();


        ButtonCellEnrollCourse() {
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            cellButton.setGraphic(imageView);
            cellButton.getStyleClass().add("mfx-btn-Enter");

            cellButton.setOnAction((ActionEvent t) -> {
                CourseDto courseDto = ButtonCellEnrollCourse.this.getTableView().getItems().get(ButtonCellEnrollCourse.this.getIndex());
                enrollStudentInCourse(courseDto.getId());
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    private class ButtonCellUnEnrollCourse extends TableCell<CourseDto, Boolean> {

        final MFXButton cellButton = new MFXButton("Delete");
        ImageView imageView = new ImageView();


        ButtonCellUnEnrollCourse() {
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            cellButton.setGraphic(imageView);
            cellButton.getStyleClass().add("mfx-btn-Delete");

            cellButton.setOnAction((ActionEvent t) -> {
                CourseDto courseDto = (CourseDto) EnrollStudentCourseController.ButtonCellUnEnrollCourse.this.getTableView().getItems().get(EnrollStudentCourseController.ButtonCellUnEnrollCourse.this.getIndex());
                unEnrollStudentInCourse(courseDto.getId());
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

}