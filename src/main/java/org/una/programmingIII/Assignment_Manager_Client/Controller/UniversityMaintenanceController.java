package org.una.programmingIII.Assignment_Manager_Client.Controller;


import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UniversityInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.input.MouseEvent;
import org.una.programmingIII.Assignment_Manager_Client.Util.Message;

public class UniversityMaintenanceController extends Controller {
    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnEdit;

    @FXML
    private MFXButton btnNew;

    @FXML
    private MFXButton btnSave;

    @FXML
    private TableColumn<UniversityDto, Long> idColumn;

    @FXML
    private MFXTextField idTxf;

    @FXML
    private TableColumn<UniversityDto, String> locationColumn;

    @FXML
    private TableColumn<UniversityDto, String> nameColumn;

    @FXML
    private MFXTextField txfLocation;

    @FXML
    private MFXTextField txfName;

    @FXML
    private TableView<UniversityDto> universityTable;

    private UniversityService universityService;
    private UniversityDto universityDto;
    private UniversityInput universityInput;
    List<Node> requeridos;

    @Override
    public void initialize() {
        requeridos = new ArrayList<>();
        universityService = new UniversityService();
        universityDto = new UniversityDto();
        universityInput = new UniversityInput();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        universityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        loadUniversities();
    }

    private void loadUniversities() {
        try {
            List<UniversityDto> universityDtoList = universityService.getAllUniversities();
            ObservableList<UniversityDto> universityDtoObservableList = FXCollections.observableArrayList(universityDtoList);
            universityTable.setItems(universityDtoObservableList);
        } catch (java.net.ConnectException e) {
            showAlert("Connection Error", "Failed to connect to the server. Please make sure the server is running.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error Loading Universities", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onActionBtnEditFaculties(ActionEvent event) {
        clean();
        loadUniversities();
    }

    @FXML
    void onActionBtnNew(ActionEvent event) throws Exception {
        String invalids = validarRequeridos();
        if (invalids.isBlank()) {
            System.out.println(invalids);
            new Message().showModal(Alert.AlertType.ERROR, "Crear universidad", getStage(), "Existen espacios  importantes en blanco");
        } else {
            universityInput = new UniversityInput();
            universityInput.setName(txfName.getText());
            universityInput.setLocation(txfLocation.getText());
            universityService.createUniversity(universityInput);
            loadUniversities();
        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (!(idTxf.getText().isBlank())) {
            convertirADto();
            universityDto = universityService.updateUniversity(universityDto.getId(), universityInput);
            loadUniversities();
        }
    }

    @FXML
    void onActionBtnDelete(ActionEvent event) throws Exception {
        if (!(idTxf.getText().isEmpty())) {
            universityService.deleteUniversity(universityDto.getId());
            clean();
            loadUniversities();
        } else {
            System.out.println("Seleccionar universidad a eliminar");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(title);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void onMouseClickedImgClose(MouseEvent event) {
        System.out.println("pushed");
        ((Stage) universityTable.getScene().getWindow()).close();
    }

    @FXML
    void onMousePressedUniversityTable(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
            universityDto = universityTable.getSelectionModel().getSelectedItem();
            convetirainput();
            System.out.println(universityInput);

        }
    }

    private void convetirainput() {
        this.universityInput.setId(universityDto.getId());
        this.universityInput.setName(universityDto.getName());
        this.universityInput.setLocation(universityDto.getLocation());
        idTxf.setText(universityInput.getId().toString());
        txfName.setText(universityInput.getName());
        txfLocation.setText(universityInput.getLocation());
    }

    private void convertirADto() {
        universityInput.setName(txfName.getText());
        universityInput.setLocation(txfLocation.getText());

        if (universityInput.getId() != null &&
                !(universityInput.getId().equals(universityDto.getId().toString()))) {
            extractId();
        }
        universityDto.setLocation(universityInput.getLocation());
        universityDto.setName(universityInput.getName());
    }

    void extractId() {
        try {
            universityDto.setId(universityDto.getId());
        } catch (NumberFormatException e) {
            System.err.println("Error: No se puede convertir a Long");
        }
    }

    private void clean() {
        this.idTxf.clear();
        this.txfLocation.clear();
        this.txfName.clear();
    }

    private void indicateRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txfLocation, txfName, idTxf));
    }

    public String validarRequeridos() {
        boolean valids = true;
        String invalidSpaces = "";
        for (Node node : requeridos) {
            if (node instanceof MFXTextField && (((MFXTextField) node).getText() == null || ((MFXTextField) node).getText().isBlank())) {
                if (valids) {
                    invalidSpaces += ((MFXTextField) node).getFloatingText();
                } else {
                    invalidSpaces += "," + ((MFXTextField) node).getFloatingText();
                }
                valids = false;
            }
        }
        if (valids) {
            return "";
        } else {
            return "Campos requeridos o con problemas de formato [" + invalidSpaces + "].";
        }
    }

}
