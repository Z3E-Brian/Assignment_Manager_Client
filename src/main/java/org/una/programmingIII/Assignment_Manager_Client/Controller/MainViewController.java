package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Interfaces.SessionObserver;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainViewController extends Controller implements SessionObserver {

    @FXML
    private MenuButton btnCoursesMenu;

    @FXML
    private ImageView facebookIcon;

    @FXML
    private Button imageButton;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView instagramIcon;

    @FXML
    private Label lblUserName;

    @FXML
    private BorderPane root;

    @FXML
    private VBox vboxCenterView;

    @FXML
    private MFXButton btnUniversitiesMaintenance;

    @FXML
    private MFXButton btnRegisterStudents_Courses, btnUserMaintenance;

    private LoginResponse loginResponse;
    private List<CourseDto> courses;
    private boolean isStudentSession;

    @Override
    public void initialize() {
        activateButton(btnRegisterStudents_Courses, false);
        activateButton(btnUniversitiesMaintenance, false);
        activateButton(btnUserMaintenance, false);
        btnCoursesMenu.setVisible(false);
        checkSession();
        loadLoginResponse();
        loadCourses();
        lblUserName.setText(SessionManager.getInstance().getLoginResponse().getUser().getFullName());
        SessionManager.getInstance().addObserver(this);
        SessionManager.getInstance().setRunningTokenValidationThread(true);
        SessionManager.getInstance().startTokenValidationTask();
        restoreBackgroundImage();
    }

    @FXML
    void onActionBtnCoursesMenu(ActionEvent event) {

    }

    private void loadCourses() {
        try {
            if (isStudentSession) {
                courses = (new CourseService().getAssociateCourses(SessionManager.getInstance().getLoginResponse().getUser().getId()));
            } else {
                courses = (new CourseService().getProfessorCourses(SessionManager.getInstance().getLoginResponse().getUser().getId()));
            }
            btnCoursesMenu.setVisible(true);

            if (courses.isEmpty()) {
                courses = new ArrayList<>();
                btnCoursesMenu.setVisible(false);
            }

            for (CourseDto course : courses) {
                MenuItem menuItem = new MenuItem(course.getName());
                menuItem.setOnAction(event -> handleMenuItemAction(menuItem));
                btnCoursesMenu.getItems().add(menuItem);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @FXML
    void onActionBtnModifyUsers(ActionEvent event) {
        removeBackgroundImage();
        FlowController.getInstance().goView("UserView");
    }

    @FXML
    void onCustomUser(ActionEvent event) {
        removeBackgroundImage();
        FlowController.getInstance().goView("MyUserView");
    }

    @FXML
    void OnActionBtnUniversitiesMaintenance(ActionEvent event) {
        removeBackgroundImage();
        FlowController.getInstance().goView("UniversityMaintenanceView");
    }

    @FXML
    void OnActionBtnRegisterStudents_Courses(ActionEvent event) {
        removeBackgroundImage();
        checkStudentLogin();
    }

    private void checkStudentLogin() {
        if (isStudentSession) {
            AppContext.getInstance().set("studentDto", SessionManager.getInstance().getLoginResponse().getUser());
            FlowController.getInstance().goView("EnrollStudentCourseView");
        } else {
            FlowController.getInstance().goView("SelectStudentToEnrollView");
        }
    }

    private void handleMenuItemAction(MenuItem menuItem) {
        String selectedCourse = menuItem.getText();
        CourseDto courseDto = courses.stream().filter(course -> course.getName().equals(selectedCourse)).findFirst().get();
        AppContext.getInstance().set("course", courseDto);
        FlowController.getInstance().deleteAndLoadView("CourseView");
    }

    private void loadLoginResponse() {
        this.loginResponse = SessionManager.getInstance().getLoginResponse();
        if (loginResponse == null) {
            new Message().showModal(Alert.AlertType.ERROR, "Login Error", getStage(), "An error occurred during login");
            FlowController.getInstance().goView("LoginView");
            FlowController.getInstance().exitMain();
            FlowController.getInstance().clearLoarders();
        }
    }


    @Override
    public void onSessionExpired() {
        Platform.runLater(() -> {
            FlowController.getInstance().goViewInWindow("LogInView");
            FlowController.getInstance().exitMain();
            SessionManager.getInstance().removeObserver(this);
            FlowController.getInstance().clearLoarders();
            new Message().showModal(Alert.AlertType.INFORMATION, "Session Timeout", getStage(), "You need to log in again");
        });
    }

    public void deleteAndLoadView(String nameView) {
        FlowController.getInstance().delete(nameView);
        FlowController.getInstance().goView(nameView);
    }

    public void removeBackgroundImage() {
        vboxCenterView.getStyleClass().remove("vBox_Main");
    }

    public void restoreBackgroundImage() {
        if (!vboxCenterView.getStyleClass().contains("vBox_Main")) {
            vboxCenterView.getStyleClass().add("vBox_Main");
        }
    }

    private void checkSession() {
        Set<PermissionDto> loginUserPermissions = SessionManager.getInstance()
                .getLoginResponse()
                .getUser()
                .getPermissions();

        checkAndActivateButton(loginUserPermissions, PermissionType.TAKE_CLASSES, btnRegisterStudents_Courses);
        checkAndActivateButton(loginUserPermissions, PermissionType.REGISTER_STUDENT_COURSES, btnRegisterStudents_Courses);
        checkAndActivateButton(loginUserPermissions, PermissionType.VIEW_USERS, btnUserMaintenance);
        checkAndActivateButton(loginUserPermissions, PermissionType.VIEW_UNIVERSITIES, btnUniversitiesMaintenance);

        if (loginUserPermissions.stream().anyMatch(permission -> PermissionType.VIEW_COURSES.equals(permission.getName()))) {
            btnCoursesMenu.setVisible(true);
        }
        if (loginUserPermissions.stream().anyMatch(permission -> PermissionType.TAKE_CLASSES.equals(permission.getName()))) {
            isStudentSession = true;
            btnRegisterStudents_Courses.setText("Enroll Courses");
        } else if (loginUserPermissions.stream().anyMatch(permission -> PermissionType.REGISTER_STUDENT_COURSES.equals(permission.getName()))) {
            isStudentSession = false;
            btnRegisterStudents_Courses.setText("Enroll Student Courses");
        }

    }

    private void checkAndActivateButton(Set<PermissionDto> permissions, PermissionType permissionType, MFXButton
            button) {
        boolean hasPermission = permissions.stream()
                .anyMatch(permission -> permissionType.equals(permission.getName()));
        activateButton(button, hasPermission);
    }


    private void activateButton(MFXButton button, boolean value) {
        button.setDisable(!value);
        button.setVisible(value);
    }

    @FXML
    void OnActionBtnLogOut(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("LogInView");
        FlowController.getInstance().exitMain();
        SessionManager.getInstance().removeObserver(this);
        FlowController.getInstance().clearLoarders();
    }

}

