package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.List;

public class SelectStudentToEnrollController extends Controller {

    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvSearch;
    @FXML
    private TableColumn<UserDto, Boolean> tbcEnrollCourses;

    @FXML
    private TableColumn<UserDto, String> tbcStudentIdentificationNumber;

    @FXML
    private TableColumn<UserDto, String> tbcStudentName;

    @FXML
    private TableColumn<UserDto, String> tbcStudentLastName;

    @FXML
    private TableView<UserDto> tbvStudents;

    Long careerId;

    @Override
    public void initialize() {
        tbcStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcStudentLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tbcStudentIdentificationNumber.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        tbcEnrollCourses.setCellValueFactory((TableColumn.CellDataFeatures<UserDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcEnrollCourses.setCellFactory((TableColumn<UserDto, Boolean> p) -> new ButtonCellEnrollCourse());
        tbvStudents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        loadCareerId();
        loadStudents();
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().delete("SelectStudentToEnrollView");
        FlowController.getInstance().goMain();
    }


    private void loadCareerId() {
        careerId = SessionManager.getInstance().getLoginResponse().getUser().getCareerId();
    }

    private void loadStudents() {
        try {
            Answer answer = new UserService().getAllStudentsByCareerId(careerId);
            if (answer.getState()) {
                List<UserDto> students = (List<UserDto>) answer.getResult("students");
                ObservableList<UserDto> studentsDtoObservableList = FXCollections.observableArrayList(students);
                tbvStudents.getItems().clear();
                tbvStudents.setItems(studentsDtoObservableList);
            } else {
                new Message().showModal(Alert.AlertType.ERROR, "Login Error", getStage(), "An error occurred during login");
            }
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Connection Error", getStage(), "Can't retrieve courses");
        }
    }

    private class ButtonCellEnrollCourse extends ButtonCellBase<UserDto> {
        ButtonCellEnrollCourse() {
            super("Enroll", "mfx-btn-Enroll");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            UserDto studentDto = SelectStudentToEnrollController.ButtonCellEnrollCourse.this.getTableView().getItems().get(SelectStudentToEnrollController.ButtonCellEnrollCourse.this.getIndex());
            AppContext.getInstance().set("studentDto", studentDto);
            FlowController.getInstance().deleteAndLoadView("EnrollStudentCourseView");
        }
    }
}
