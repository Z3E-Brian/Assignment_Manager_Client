package org.una.programmingIII.Assignment_Manager_Client.Controller;

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

import java.io.IOException;

public class MainViewController extends Controller {

    @FXML
    public ImageView imageView;
    @FXML
    public Button imageButton;
    @FXML
    public ImageView facebookIcon;
    @FXML
    public ImageView instagramIcon;
    @FXML
    public MenuButton coursesMenuButton;
    @FXML
    public BorderPane mainBorderPane;

    @Override
    public void initialize() { //TODO: ADD EVERY STUFF DYNAMICALLY (COURSES, ETC)
        coursesMenuButton.getItems().add(new MenuItem("Course 1"));
        coursesMenuButton.getItems().add(new MenuItem("Course 2"));

        coursesMenuButton.getItems().forEach(item -> item.setOnAction(event -> {
            System.out.println("Switching to Course View");
            try {
                setCenterView("CourseView"); //TODO: ADD THE LOGIC TO GET INFO ABOUT THE COURSE
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

    }

    // Método para cambiar el contenido en la parte central (center) del BorderPane
    public void setCenterView(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/una/programmingIII/Assignment_Manager_Client/View/" + fxmlName + ".fxml"));
        Parent newView = loader.load();
        mainBorderPane.setCenter(newView);
    }

    @FXML
    public void onCustomUser() {
        try {
            System.out.println("Changing view to AddAssignmentView");
            setCenterView("UserView");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCustomAssignment() {
        try {
            System.out.println("Changing view to AddAssignmentView");
            setCenterView("AssignmentView");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

