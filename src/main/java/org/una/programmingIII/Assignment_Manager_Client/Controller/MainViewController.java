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
    private MFXButton btnRegisterStudents_Courses;


    private LoginResponse loginResponse;
    private List<CourseDto> courses;
    private boolean isStudentSession;

    @Override
    public void initialize() { //TODO: ADD EVERY STUFF DYNAMICALLY (COURSES, ETC)
        loadCourses();
        for (CourseDto course : courses) {
            MenuItem menuItem = new MenuItem(course.getName());
            menuItem.setOnAction(event -> handleMenuItemAction(menuItem));
            btnCoursesMenu.getItems().add(menuItem);

        }
        loadLoginResponse();
        lblUserName.setText(SessionManager.getInstance().getLoginResponse().getUser().getFullName());
        SessionManager.getInstance().addObserver(this);
        SessionManager.getInstance().setRunningTokenValidationThread(true);
        SessionManager.getInstance().startTokenValidationTask();
        restoreBackgroundImage();
        checkSession();
    }

    @FXML
    void onActionBtnCoursesMenu(ActionEvent event) {

    }

    private void loadCourses() {
        try {
            courses = (new CourseService().getCoursesByCareerId(SessionManager.getInstance().getLoginResponse().getUser().getCareerId()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    void onCustomUser(ActionEvent event) {
        removeBackgroundImage();
        FlowController.getInstance().goView("UserView");
    }

    @FXML
    void OnActionBtnUniversitiesMaintenance(ActionEvent event) {
        removeBackgroundImage();
        FlowController.getInstance().goView("UniversityMaintenanceView");
    }

    @FXML
    void OnActionBtnRegisterStudents_Courses(ActionEvent event) {
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
        FlowController.getInstance().goView("CourseView");
    }

    private void loadLoginResponse() {
        this.loginResponse = SessionManager.getInstance().getLoginResponse();
        if (loginResponse == null) {
            new Message().showModal(Alert.AlertType.ERROR, "Error inicio sesion", getStage(), "Ha ocurrido un error al iniciar sesion");
            FlowController.getInstance().goView("LoginView");
            FlowController.getInstance().exitMain();
            FlowController.getInstance().clearLoarders();
        } else {
            loadPermisosUsers();
        }
    }

    private void loadPermisosUsers() {
        System.out.println("Permisos de uso");
        System.out.println(SessionManager.getInstance().getLoginResponse().getUser().getPermissions());
        Set<PermissionDto> permissionDtos = SessionManager.getInstance().getLoginResponse().getUser().getPermissions();
        boolean hasViewCoursesPermission = permissionDtos.stream()
                .anyMatch(permission -> permission.getName() == PermissionType.VIEW_COURSES);

        if (hasViewCoursesPermission) {
            System.out.println("El usuario tiene permiso para ver cursos.");
            this.btnCoursesMenu.setDisable(true);
        } else {
            System.out.println("El usuario NO tiene permiso para ver cursos.");
        }

    }

    @Override
    public void onSessionExpired() {
        Platform.runLater(() -> {
            FlowController.getInstance().goViewInWindow("LogInView");
            FlowController.getInstance().exitMain();
            SessionManager.getInstance().removeObserver(this);
            FlowController.getInstance().clearLoarders();
            new Message().showModal(Alert.AlertType.INFORMATION, "Tiempo de inicio de sesion agotado", getStage(), "Debes de volver a iniciar sesion");
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

        boolean hasProfessorPermission = loginUserPermissions.stream()
                .anyMatch(permission -> PermissionType.TAKE_CLASSES.equals(permission.getName()));

        if (hasProfessorPermission) {
            isStudentSession = true;
            btnRegisterStudents_Courses.setText("Enroll Courses");
        }
    }


}

