package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class CreateCourseViewController extends Controller {


    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXComboBox<UserDto> cbxProfessor;

    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvClose;

    @FXML
    private ImageView imvSearch;

    @FXML
    private TableColumn<CourseDto, String> tbcDescription;

    @FXML
    private TableColumn<CourseDto, String> tbcName;

    @FXML
    private TableColumn<UserDto, String> tbcProfessor;

    @FXML
    private TableView<CourseDto> tbvCourse;

    @FXML
    private MFXTextField txfDescription;

    @FXML
    private MFXTextField txfName;

    CourseInput courseInput;
    List<UserDto> professors;
    ArrayList<Node> required = new ArrayList<>();
    UserDto professorDto;

    @Override
    public void initialize() {
        txfName.delegateSetTextFormatter(Format.getInstance().textFormat(150));
        txfDescription.delegateSetTextFormatter(Format.getInstance().textFormat(1000));
        cbxProfessor.getItems().addAll(professors);
        indicateRequired();
    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {
        try {
            if (courseInput.getId() == null) {
                showError("Delete Course", "You must upload the course to be deleted");
            } else {
                Answer answer = new CourseService().deleteCourse(courseInput.getId());
                if (!answer.getState()) {
                    showError("Delete Course", answer.getMessage());
                } else {
                    showInfo("Delete Course", answer.getMessage());
                    newCourse();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            showError("Delete Course", e.getMessage());
        }
    }

    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (new Message().showConfirmation("New Course", getStage(), "Are you sure you want to clean the registry?")) {
            newCourse();
        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) {
        try {
            String validationMessage = validateRequired();
            if (!validationMessage.isEmpty()) {
                showError("Save Course", validationMessage);
                return;
            }
            Answer answer = new CourseService().createCourse(courseInput);
            answer.setState(true);
            if (!answer.getState()) {
                showError("Save Course", answer.getMessage());
            } else {
                showInfo("Save Course", answer.getMessage());
                courseInput = (CourseInput) answer.getResult("course");
                newCourse();
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            showError("Save Course", "An error occurred saving the assignment");
        }
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {

    }

    @FXML
    void onMouseClickedImvClose(MouseEvent event) {

    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {

    }

    @FXML
    void onMousePressedUniversityTable(MouseEvent event) {

    }

    @FXML
    void onActionCbxProfessor(ActionEvent event) {
        professorDto = cbxProfessor.getValue();
    }

    private void newCourse() {
        courseInput = new CourseInput();
        unbindAssignment();
        bindAssignment();
        txfName.requestFocus();
    }

    private void bindAssignment() {
        txfName.textProperty().bindBidirectional(courseInput.name);
        txfDescription.textProperty().bindBidirectional(courseInput.description);
        cbxProfessor.setValue(courseInput.professor);

    }

    private void unbindAssignment() {
        txfName.textProperty().bindBidirectional(courseInput.name);
        txfDescription.textProperty().bindBidirectional(courseInput.description);

    }

    private void indicateRequired() {
        required.clear();
        required.addAll(Arrays.asList(txfName, txfDescription, cbxProfessor));
    }

    private String validateRequired() {
        StringBuilder invalid = new StringBuilder();
        for (Node node : required) {
            String floatingText = getFloatingText(node);
            if (floatingText != null) {
                if (!invalid.isEmpty()) {
                    invalid.append(", ");
                }
                invalid.append(floatingText);
            }
        }
        return invalid.isEmpty() ? "" : "Fields required or with formatting problems [" + invalid + "].";
    }

    private String getFloatingText(Node node) {
        String floatingText = null;
        if (node instanceof MFXTextField && isEmpty(((MFXTextField) node).getText())) {
            floatingText = ((MFXTextField) node).getFloatingText();
        } else if (node instanceof MFXComboBox && ((MFXComboBox<?>) node).getValue() == null) {
            floatingText = ((MFXComboBox<?>) node).getFloatingText();
        }
        return floatingText;
    }

    private boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }
}