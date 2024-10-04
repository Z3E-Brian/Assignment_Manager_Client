package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController extends Controller implements Initializable {

    @Override
    public void initialize() {
    }

    public void onAddUser() {
        System.out.println("Add User");
    }

    public void onEditUser() {
        System.out.println("Edit User");
    }

    @FXML
    public void goBack() {
//        System.out.println("Going back");
//        try {
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
