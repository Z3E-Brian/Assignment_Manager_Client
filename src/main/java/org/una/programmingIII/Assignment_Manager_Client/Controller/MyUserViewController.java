package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private MFXButton btnEdit;
    @FXML
    private ListView<String> coursesListView;
    @FXML
    private Label careerLabel, nameLabel, lastNameLabel, secondLastNameLabel, emailLabel, identificationNumberLabel;

    private List<CourseDto> courses;

    @Override
    public void initialize() {
        clear();
        loadUserInformation();
        btnEdit.setVisible(SessionManager.getInstance().getLoginResponse().getUser().getPermissions().stream()
                .anyMatch(permission -> PermissionType.EDIT_PROFILE.equals(permission.getName())));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }

    private void clear() {
        nameLabel.setText("");
        lastNameLabel.setText("");
        secondLastNameLabel.setText("");
        emailLabel.setText("");
        identificationNumberLabel.setText("");
        careerLabel.setText("");
        coursesListView.getItems().clear();
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
        try {
            Answer answer = new CareerService().getById(careerId);
            if (answer.getState()) {
                CareerDto careerDto = (CareerDto) answer.getResult("careerDto");
                careerLabel.setText(careerDto.getName());
            } else {
                showError("Load Career", "Can't load the career label correctly");
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
            if (courses.isEmpty()) courses = new ArrayList<>();
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
}