package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CareerInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.DepartmentService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CareerMaintenanceViewController extends Controller {
    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvSearch;

    @FXML
    private Label lblDepartment;

    @FXML
    private TableColumn<CareerDto, String> tbcDescription;

    @FXML
    private TableColumn<CareerDto, String> tbcName;

    @FXML
    private TableView<CareerDto> tbvCareer;

    @FXML
    private MFXTextField txfDescription;

    @FXML
    private MFXTextField txfName;
    @FXML
    private TableColumn<CareerDto, Boolean> tbcDelete;
    @FXML
    private TableColumn<CareerDto, Boolean> tbcCourse;

    DepartmentDto departmentDto;
    CareerInput careerInput;
    CareerDto careerDto;
    private RequiredFieldsValidator validator;

    @Override
    public void initialize() {
        initializeCareerData();
        setupTableColumns();
        setupTextFormatters();
        setupValidator();
        loadDepartment();
        loadCareers();
        bind();
    }

    private void initializeCareerData() {
        careerInput = new CareerInput();
        careerDto = new CareerDto();
        departmentDto = new DepartmentDto();
    }

    private void setupTableColumns() {
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbcDelete.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tbcDelete.setCellFactory(p -> new ButtonCellDelete());
        tbcCourse.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tbcCourse.setCellFactory(p -> new ButtonCellCourse());
        tbvCareer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void setupTextFormatters() {
        txfName.delegateSetTextFormatter(Format.getInstance().textFormat(80));
        txfDescription.delegateSetTextFormatter(Format.getInstance().textFormat(300));
    }

    private void setupValidator() {
        validator = new RequiredFieldsValidator(Arrays.asList(txfName, txfDescription));
    }

    private void bind() {
        txfDescription.textProperty().bindBidirectional(careerInput.description);
        txfName.textProperty().bindBidirectional(careerInput.name);
    }

    private void unbind() {
        txfDescription.textProperty().unbindBidirectional(careerInput.description);
        txfName.textProperty().unbindBidirectional(careerInput.name);
    }

    private void loadCareers() {
        try {
            departmentDto = new DepartmentService().getById(departmentDto.getId());
            List<CareerDto> departmentDtoList = departmentDto.getCareers();
            ObservableList<CareerDto> careerDtoObservableList = FXCollections.observableArrayList(departmentDtoList);
            tbvCareer.getItems().clear();
            tbvCareer.setItems(careerDtoObservableList);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.WARNING, "Connection Error", getStage(), "You must select one of the universities in the table to delete it.");

        }
    }

    private void clean() {
        unbind();
        txfDescription.setText("");
        txfName.setText("");
        careerInput = new CareerInput();
        careerDto = new CareerDto();
        bind();
    }


    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (new Message().showConfirmation("New Career", getStage(), "Are you sure you want to clean the registry?")) {
            clean();
        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) {

        try {
            String validationMessage = validator.validate();
            if (!validationMessage.isEmpty()) {
                showError("Save Career", validationMessage);
                return;
            }

            careerDto = new CareerDto(careerInput);
            careerDto.setDepartmentId(departmentDto.getId());

            Answer answer = new CareerService().createCareer(careerDto);
            if (!answer.getState()) {
                showError("Save Course", answer.getMessage());
            } else {
                showInfo("Save Course", answer.getMessage());
                clean();
                loadCareers();
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            showError("Save Course", "An error occurred saving the assignment");
        }
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().deleteAndLoadView("DepartmentMaintenanceView");
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {

    }

    @FXML
    void onMousePressedCareerTable(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1 && tbvCareer.getSelectionModel().getSelectedItem() != null) {
            careerDto = tbvCareer.getSelectionModel().getSelectedItem();
            unbind();
            careerInput = new CareerInput(careerDto);
            bind();
        }
    }

    private void loadDepartment() {
        departmentDto = (DepartmentDto) AppContext.getInstance().get("departmentDto");
        lblDepartment.setText(departmentDto.getName());
    }

    private void showError(String title, String message) {
        new Message().showModal(Alert.AlertType.ERROR, title, getStage(), message);
    }

    private void showInfo(String title, String message) {
        new Message().showModal(Alert.AlertType.INFORMATION, title, getStage(), message);
    }

    private void deleteCareer(CareerDto career) {
        try {
            Answer answer = new CareerService().deleteCareer(career.getId());
            if (!answer.getState()) {
                showError("Delete Career", answer.getMessage());
            } else {
                showInfo("Delete Career", answer.getMessage());
            }
        } catch (Exception e) {
            showError("Delete Career", "An error occurred deleting the career");
        }
    }

    private class ButtonCellDelete extends ButtonCellBase<CareerDto> {
        ButtonCellDelete() {
            super("Delete", "mfx-btn-Delete");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            CareerDto career = getTableView().getItems().get(getIndex());
            deleteCareer(career);
            loadCareers();
        }
    }

    private class ButtonCellCourse extends ButtonCellBase<CareerDto> {
        ButtonCellCourse() {
            super("Course", "mfx-btn-Enter");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            careerDto = getTableView().getItems().get(getIndex());
            AppContext.getInstance().set("careerDto", careerDto);
            FlowController.getInstance().deleteAndLoadView("CreateCourseView");
        }
    }


}