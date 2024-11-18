package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyUserViewController extends Controller implements Initializable {

    @FXML
    private ListView<String> coursesListView;
    @FXML
    private Label careerLabel, nameLabel, lastNameLabel, secondLastNameLabel, emailLabel, identificationNumberLabel;
    @FXML
    private Label noCoursesLabel;

    private List<CourseDto> courses;

    @Override
    public void initialize() {
        clear();
        loadUserInformation();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void clear() {
        nameLabel.setText("N/A");
        lastNameLabel.setText("N/A");
        secondLastNameLabel.setText("N/A");
        emailLabel.setText("N/A");
        identificationNumberLabel.setText("N/A");
        careerLabel.setText("N/A");
        coursesListView.getItems().clear();
        noCoursesLabel.setVisible(false);
    }

    private void loadUserInformation() {
        UserDto user = SessionManager.getInstance().getLoginResponse().getUser();
        nameLabel.setText(user.getName());
        lastNameLabel.setText(user.getLastName());
        secondLastNameLabel.setText(user.getSecondLastName());
        emailLabel.setText(user.getEmail());
        identificationNumberLabel.setText(user.getIdentificationNumber());
        loadCareer(user.getCareerId());
        loadCourses();
    }

    private void loadCareer(Long careerId) {
        if (careerId == null) {
            showWarning("Load Career", "You don't have a career assigned");
            return;
        }
        try {
            Answer answer = new CareerService().getById(careerId);
            if (answer.getState()) {
                CareerDto careerDto = (CareerDto) answer.getResult("careerDto");
                careerLabel.setText(careerDto.getName());
            } else {
                showError("Load Career", "You don't have a career assigned");
            }
        } catch (Exception e) {
            showError("Load Career", "Can't load the career label correctly");
        }
    }

    private void loadCourses() {
        try {
            UserDto user = SessionManager.getInstance().getLoginResponse().getUser();
            boolean privilege = user.getPermissions().stream()
                    .anyMatch(permission -> PermissionType.CREATE_ASSIGNMENTS.equals(permission.getName()));
            Long careerId = user.getCareerId();
            courses = privilege ? new CourseService().getAssociateCourses(user.getId())
                    : new CourseService().getCoursesByCareerId(careerId);
            if (courses.isEmpty()) {
                courses = new ArrayList<>();
                showWarning("Load Courses", "You don't have courses assigned");
                coursesListView.setVisible(false);
                noCoursesLabel.setVisible(true);
                return;
            }
            noCoursesLabel.setVisible(false);
            coursesListView.getItems().addAll(courses.stream().map(CourseDto::getName).toList());
            coursesListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && coursesListView.getSelectionModel().getSelectedItem() != null) {
                    CourseDto course = courses.get(coursesListView.getSelectionModel().getSelectedIndex());
                    AppContext.getInstance().set("course", course);
                    FlowController.getInstance().goView("CourseView");
                }
            });
        } catch (Exception e) {
            showError("Load Courses", "Can't load the courses list correctly");
        }
    }

    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showWarning(String title, String message) {
        new Message().showModal(Alert.AlertType.WARNING, title, getStage(), message);
    }
}