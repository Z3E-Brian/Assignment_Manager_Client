package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
    private TableColumn<CourseDto, Boolean> tbcAvailableCourses;

    @FXML
    private TableView<CourseDto> tbvAvailableCourses;

    @FXML
    private TableView<?> tbvEnrollCourses;

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

        tbcUnenroll.setCellValueFactory((TableColumn.CellDataFeatures<CourseDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcUnenroll.setCellFactory((TableColumn<CourseDto, Boolean> p) -> new ButtonCellUnEnrollCourse());
        tbcEnroll.setCellValueFactory((TableColumn.CellDataFeatures<CourseDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcEnroll.setCellFactory((TableColumn<CourseDto, Boolean> p) -> new ButtonCellEnrollCourse());
        tbvAvailableCourses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }


    @FXML
    void OnMousePressedTbvCourses(MouseEvent event) {

    }

    @FXML
    void OnMousePressedTbvEnrollCourses(MouseEvent event) {

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

    private void cargarCursos() {
        cargarCursosMatriculadoUsuario();
        cargarCursosDisponibles();
    }

    private void cargarCursosMatriculadoUsuario() {

    }

    private void cargarCursosDisponibles() {
    }

    private void loadInitialData() {
        studentDto = SessionManager.getInstance().getLoginResponse().getUser();
        lblStudentName.setText(studentDto.getName());
        loadCareerInfo();
        cargarCursos();
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
//                CareerDto career = (CareerDto) CareerMaintenanceViewController.ButtonCellDelete.this.getTableView().getItems().get(CareerMaintenanceViewController.ButtonCellDelete.this.getIndex());
//                deleteCareer(career);
//                loadCareers();
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

    private class ButtonCellEnrollCourse extends TableCell<CourseDto, Boolean> {

        final MFXButton cellButton = new MFXButton("Course");
        ImageView imageView = new ImageView();


        ButtonCellEnrollCourse() {
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            cellButton.setGraphic(imageView);
            cellButton.getStyleClass().add("mfx-btn-Enter");

            cellButton.setOnAction((ActionEvent t) -> {
//                careerDto = (CareerDto) CareerMaintenanceViewController.ButtonCellCourse.this.getTableView().getItems().get(CareerMaintenanceViewController.ButtonCellCourse.this.getIndex());
//                AppContext.getInstance().set("careerDto", careerDto);
//                FlowController.getInstance().goViewInWindow("CreateCourseView");
//                getStage().close();
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