package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.una.programmingIII.Assignment_Manager_Client.Dto.*;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class CreateCourseViewController extends Controller {


    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private MFXComboBox<UserDto> cbxProfessor;

    @FXML
    private ImageView imvBack;


    @FXML
    private ImageView imvSearch;

    @FXML
    private MFXDatePicker dtpEndDate;

    @FXML
    private MFXDatePicker dtpStartDate;

    @FXML
    private TableColumn<CourseDto, String> tbcDescription;

    @FXML
    private TableColumn<CourseDto, String> tbcName;

    @FXML
    private TableColumn<UserDto, String> tbcProfessor;


    @FXML
    private TableColumn<CourseDto, Boolean> tbcDelete;


    @FXML
    private TableView<CourseDto> tbvCourse;

    @FXML
    private MFXTextField txfDescription;

    @FXML
    private MFXTextField txfName;

    @FXML
    private Label lblCareer;

    private CourseInput courseInput;
    private List<UserDto> professors;
    private ArrayList<Node> required = new ArrayList<>();
    private UserDto professorDto;
    private CareerDto careerDto;
    private CourseDto courseDto;
    private RequiredFieldsValidator validator;

    @Override
    public void initialize() {
        initializeCourseData();
        setupTableColumns();
        setupTextFormatters();
        loadProfessorsAndCareer();
        setupValidator();
        bindCourse();
    }

    private void initializeCourseData() {
        courseInput = new CourseInput();
        courseDto = new CourseDto();
        careerDto = new CareerDto();
    }

    private void setupTableColumns() {
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbcProfessor.setCellValueFactory(new PropertyValueFactory<>("professor"));
        tbcDelete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tbcDelete.setCellFactory(p -> new ButtonCellDelete());
        tbvCourse.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void setupTextFormatters() {
        txfName.delegateSetTextFormatter(Format.getInstance().textFormat(150));
        txfDescription.delegateSetTextFormatter(Format.getInstance().textFormat(1000));
    }

    private void loadProfessorsAndCareer() {
        loanProfessors();
        loanCareer();
    }

    private void setupValidator() {
        validator = new RequiredFieldsValidator(Arrays.asList(txfName, txfDescription, cbxProfessor, dtpStartDate, dtpEndDate));
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
            String validationMessage = validator.validate();
            if (!validationMessage.isEmpty()) {
                showError("Save Course", validationMessage);
                return;
            }
            if (courseInput.getCareerId() == null) {
                courseInput.setCareerId(careerDto.getId());
            }
            Answer answer = new CourseService().createCourse(courseInput);
            if (!answer.getState()) {
                showError("Save Course", answer.getMessage());
            } else {
                showInfo("Save Course", answer.getMessage());
                cleanAll();
                tbvCourse.getItems().clear();
                getCoursesByCareer();
                newCourse();
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            showError("Save Course", "An error occurred saving the assignment");
        }
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().deleteAndLoadView("CareerMaintenanceView");
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {

    }

    @FXML
    void onMousePressedUniversityTable(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1 && tbvCourse.getSelectionModel().getSelectedItem() != null) {
            courseDto = tbvCourse.getSelectionModel().getSelectedItem();
            courseInput = new CourseInput(courseDto);
            bindCourse();
        }
    }

    @FXML
    void onActionCbxProfessor(ActionEvent event) {
        professorDto = cbxProfessor.getValue();
    }

    private void newCourse() {
        unbindCourse();
        cleanAll();
        bindCourse();
    }

    private void cleanAll() {
        txfName.clear();
        txfDescription.clear();
        cbxProfessor.getSelectionModel().clearSelection();

        dtpStartDate.setValue(null);
        dtpEndDate.setValue(null);

        courseInput = new CourseInput();
        courseDto = new CourseDto();

    }


    private void bindCourse() {
        txfName.textProperty().bindBidirectional(courseInput.name);
        txfDescription.textProperty().bindBidirectional(courseInput.description);
        cbxProfessor.valueProperty().bindBidirectional(courseInput.professor);
    }

    private void unbindCourse() {
        txfName.textProperty().unbindBidirectional(courseInput.name);
        txfDescription.textProperty().unbindBidirectional(courseInput.description);
        cbxProfessor.valueProperty().unbindBidirectional(courseInput.professor);
        cbxProfessor.clear();
    }


    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }

    private void loanCareer() {
        try {
            careerDto = (CareerDto) AppContext.getInstance().get("careerDto");
            lblCareer.setText(careerDto.getName());
            getCoursesByCareer();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getCoursesByCareer() {
        try {
            tbvCourse.getItems().clear();
            List<CourseDto> coursesByCareer = (new CourseService().getCoursesByCareerId(careerDto.getId()));
            ObservableList<CourseDto> coursesByCareerObservableList = FXCollections.observableArrayList(coursesByCareer);
            tbvCourse.setItems(coursesByCareerObservableList);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void loanProfessors() {
        try {
            Answer answer = (Answer) new UserService().getAllUsersByPermission("PROFESSOR");
            System.out.println(answer.getResult("users"));
            if (!answer.getState()) {
                showError("Load Professors", answer.getMessage());
            } else {
                professors = (List<UserDto>) answer.getResult("users");
                ObservableList<UserDto> professorList = FXCollections.observableArrayList(professors);
                cbxProfessor.setItems(professorList);
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
        }
    }

    private void deleteCourse(CourseDto courseDto) {
        try {
            if (courseDto.getId() == null) {
                showError("Delete Course", "You must upload the course to be deleted");
            } else {
                Answer answer = new CourseService().deleteCourse(courseDto.getId());
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

    private class ButtonCellDelete extends ButtonCellBase<CourseDto> {
        ButtonCellDelete() {
            super("Delete", "mfx-btn-Delete");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            CourseDto course = ButtonCellDelete.this.getTableView().getItems().get(ButtonCellDelete.this.getIndex());
            deleteCourse(course);
            cleanAll();
            tbvCourse.getItems().clear();
            getCoursesByCareer();
            newCourse();
        }

    }
}