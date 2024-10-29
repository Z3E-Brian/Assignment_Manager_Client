package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CareerInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.CareerService;
import org.una.programmingIII.Assignment_Manager_Client.Service.DepartmentService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        careerInput = new CareerInput();
        careerDto = new CareerDto();
        departmentDto = new DepartmentDto();

        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbvCareer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        txfName.delegateSetTextFormatter(Format.getInstance().textFormat(150));
        txfDescription.delegateSetTextFormatter(Format.getInstance().textFormat(1000));

        loadDepartment();
        indicateRequired();
        loadCareers();
        System.out.println(departmentDto);
    }

    private void loadCareers() {
        try {
            departmentDto = new DepartmentService().getById(departmentDto.getId());
            List<CareerDto> departmentDtoList = departmentDto.getCareers();
            ObservableList<CareerDto> careerDtoObservableList = FXCollections.observableArrayList(departmentDtoList);
            tbvCareer.getItems().clear();
            tbvCareer.setItems(careerDtoObservableList);
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.WARNING, "Error de conexion", getStage(), "Debe selecionar una de las universidades en la tabla para poder eliminarla.");

        }


    }

    @FXML
    void onActionBtnCourse(ActionEvent event) {

    }

    private void clean() {
        txfDescription.setText("");
        txfName.setText("");
        careerInput = new CareerInput();
        careerDto = new CareerDto();
    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {

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
            String validationMessage = validateRequired();
            if (!validationMessage.isEmpty()) {
                showError("Save Career", validationMessage);
                return;
            }

            if (careerInput.getDepartmentId() == null) {
                careerInput.setDepartmentId(departmentDto.getId());
            }

            careerDto.setDescription(txfDescription.getText());
            careerDto.setName(txfName.getText());
            careerDto.setDepartmentId(departmentDto.getId());


            Answer answer = new CareerService().createCareer(careerDto);
            if (!answer.getState()) {
                showError("Save Course", answer.getMessage());
            } else {
                showInfo("Save Course", answer.getMessage());
                careerInput = (CareerInput) answer.getResult("career");
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
        }
    }

    private void loadDepartment() {
        departmentDto = (DepartmentDto) AppContext.getInstance().get("departmentDto");
        lblDepartment.setText(departmentDto.getName());
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
        } else if (node instanceof MFXDatePicker && ((MFXDatePicker) node).getValue() == null) {
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


}