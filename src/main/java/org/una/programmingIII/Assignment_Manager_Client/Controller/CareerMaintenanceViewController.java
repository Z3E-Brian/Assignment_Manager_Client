package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CareerInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.CourseService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class CareerMaintenanceViewController extends Controller {


    @FXML
    private MFXButton btnCourse;

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvClose;

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
    ArrayList<Node> required = new ArrayList<>();
    DepartmentDto departmentDto;
    CareerInput careerInput;
    CareerDto careerDto;
    @Override
    public void initialize() {
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbvCareer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        txfName.delegateSetTextFormatter(Format.getInstance().textFormat(150));
        txfDescription.delegateSetTextFormatter(Format.getInstance().textFormat(1000));
        loanDepartment();
        indicateRequired();
    }

    @FXML
    void onActionBtnCourse(ActionEvent event) {

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {
        try {
            if (careerInput.getId() == null) {
                showError("Delete Career", "You must upload the career to be deleted");
            } else {
                Answer answer = new CareerService().deleteCareer(careerInput.getId());
                if (!answer.getState()) {
                    showError("Delete Career", answer.getMessage());
                } else {
                    showInfo("Delete Career", answer.getMessage());
                    newCareer();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            showError("Delete Career", e.getMessage());
        }
    }

    @FXML
    void onActionBtnNew(ActionEvent event) {
        if (new Message().showConfirmation("New Career", getStage(), "Are you sure you want to clean the registry?")) {
            newCareer();
        }

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {

        try {
            String validationMessage = validateRequired();
            if (!validationMessage.isEmpty()) {
                showError("Save Career", validationMessage);
                return;
            }
            if (careerInput.getDepartment()==null){
                careerInput.setDepartment(departmentDto);
            }
           Answer answer = new CareerService().createCareer(careerInput);
            answer.setState(true);
            if (!answer.getState()) {
                showError("Save Course", answer.getMessage());
            } else {
                showInfo("Save Course", answer.getMessage());
                careerInput = (CareerInput) answer.getResult("career");
                newCareer();
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
        if (new Message().showConfirmation("Exit", getStage(), "Are you sure you want to exit?")) {
            getStage().close();
        }
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {

    }

    @FXML
    void onMousePressedCareerTable(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1 && tbvCareer.getSelectionModel().getSelectedItem() != null) {
         careerDto = tbvCareer.getSelectionModel().getSelectedItem();
            careerInput = new CareerInput(careerDto);
            bindCourse();
        }
    }
    private void loanDepartment() {
        departmentDto = (DepartmentDto) AppContext.getInstance().get("departmentDto");
        lblDepartment.setText(departmentDto.getName());
        tbvCareer.getItems().clear();
        tbvCareer.getItems().addAll(departmentDto.getCareers());
    }
    private void indicateRequired() {
        required.clear();
        required.addAll(Arrays.asList(txfName, txfDescription));
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
        }else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
            floatingText = ((MFXDatePicker) node).getFloatingText();
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
    private void bindCourse() {
        txfName.textProperty().bindBidirectional(careerInput.name);
        txfDescription.textProperty().bindBidirectional(careerInput.description);
    }

    private void unbindAssignment() {
        txfName.textProperty().bindBidirectional(careerInput.name);
        txfDescription.textProperty().bindBidirectional(careerInput.description);
    }
    private void newCareer() {
        careerInput = new CareerInput();
        unbindAssignment();
        bindCourse();
        txfName.requestFocus();
    }
}
