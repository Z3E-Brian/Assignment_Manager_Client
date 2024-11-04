package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import org.una.programmingIII.Assignment_Manager_Client.Dto.*;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

public class MyUserViewController extends Controller implements Initializable {

    @FXML
    public MFXButton btnEdit;
    @FXML
    public ListView<String> coursesListView;
    @FXML
    private Label careerLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label secondLastNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label identificationNumberLabel;

    private List<CourseDto> courses;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }

    @Override
    public void initialize() {
        clear();
        loadUserInformation();
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
        try {
            Long careerId = user.getCareerId();
            Answer answer = new CareerService().getById(careerId);
            if (answer.getState()) {
                CareerDto careerDto = (CareerDto) answer.getResult("careerDto");
                careerLabel.setText(careerDto.getName());
            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Load Career", getStage(), "Can't load the career label correctly");
            }
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Load Career", getStage(), "Can't load the career label correctly");
        }
        loadCourses();


    }

    private void loadCourses() {
        try {
            boolean privilege = SessionManager.getInstance().getLoginResponse().getUser().getPermissions().stream()
                    .anyMatch(permission -> PermissionType.CREATE_ASSIGNMENTS.equals(permission.getName()));
            Long careerId = SessionManager.getInstance().getLoginResponse().getUser().getCareerId();
            if (privilege) {
                courses = (new CourseService().getEnrolledCoursesByStudentId(SessionManager.getInstance().getLoginResponse().getUser().getId()));
            } else {
                courses = (new CourseService().getCoursesByCareerId(careerId));
            }

            if (courses.isEmpty()) {
                courses = new ArrayList<>();
            }

            //llenar el listview de cursos
            coursesListView.getItems().addAll(courses.stream().map(CourseDto::getName).toList());
            coursesListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && coursesListView.getSelectionModel().getSelectedItem() != null) {
                    CourseDto course = courses.get(coursesListView.getSelectionModel().getSelectedIndex());
                    AppContext.getInstance().set("course", course);
                    FlowController.getInstance().goView("CourseView");
                }
            });

        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
