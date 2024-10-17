package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainViewController extends Controller {

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
    private BorderPane root;


    @Override
    public void initialize() { //TODO: ADD EVERY STUFF DYNAMICALLY (COURSES, ETC)
        List<String> courses = Arrays.asList("Course 1", "Course 2", "Course 3");
        for (String course : courses) {
            MenuItem menuItem = new MenuItem(course);
            menuItem.setOnAction(event -> handleMenuItemAction(menuItem));
            btnCoursesMenu.getItems().add(menuItem);
        }
    }

    @FXML
    void onActionBtnCoursesMenu(ActionEvent event) {
    }
    private void handleMenuItemAction(MenuItem menuItem) {
        String selectedCourse = menuItem.getText();
        System.out.println("Selected course: " + selectedCourse);
        if (selectedCourse.equals("Course 1")) {
            FlowController.getInstance().goView("CourseView");
        } else if (selectedCourse.equals("Course 2")) { FlowController.getInstance().goView("UploadTaskView");}

    }
    @FXML
    void onCustomUser(ActionEvent event) {
        FlowController.getInstance().goView("AssignmentView");
    }


}

