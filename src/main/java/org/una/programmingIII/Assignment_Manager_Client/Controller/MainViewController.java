package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;
import org.una.programmingIII.Assignment_Manager_Client.Interfaces.SessionObserver;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;

import java.util.Arrays;
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

    private LoginResponse loginResponse;


    @Override
    public void initialize() { //TODO: ADD EVERY STUFF DYNAMICALLY (COURSES, ETC)
        List<String> courses = Arrays.asList("Course 1", "Course 2", "Course 3");
        
        for (String course : courses) {
            MenuItem menuItem = new MenuItem(course);
            menuItem.setOnAction(event -> handleMenuItemAction(menuItem));
            btnCoursesMenu.getItems().add(menuItem);

        }
        loadLoginResponse();
        lblUserName.setText(SessionManager.getInstance().getLoginResponse().getUser().getFullName());
        SessionManager.getInstance().addObserver(this);
        SessionManager.getInstance().setRunningTokenValidationThread(true);
        SessionManager.getInstance().startTokenValidationTask();
    }

    @FXML
    void onActionBtnCoursesMenu(ActionEvent event) {
    }

    @FXML
    void onCustomUser(ActionEvent event) {
        FlowController.getInstance().goView("UserView");
    }

    private void handleMenuItemAction(MenuItem menuItem) {
        String selectedCourse = menuItem.getText();
        System.out.println("Selected course: " + selectedCourse);
        switch (selectedCourse) {
            case "Course 1":
                FlowController.getInstance().goView("CourseView");
                break;
            case "Course 2":
                FlowController.getInstance().goView("UploadTaskView");
                break;
            case "Course 3":
                FlowController.getInstance().goView("AddAssignmentOrFileView");
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + selectedCourse);
        }
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
}

