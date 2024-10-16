package org.una.programmingIII.Assignment_Manager_Client.Controller;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UniversityInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Mapper.GenericMapper;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

import java.util.List;

import javafx.scene.input.MouseEvent;

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
    private GenericMapper<UniversityInput, UniversityDto> universityMapper;

    @Override
    public void initialize() {

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
        loadUniversities();
    }

    @FXML
    void onActionBtnNew(ActionEvent event) throws Exception {
        System.out.println("New University");
        universityInput = new UniversityInput();
        bindUniversity(true);
        //universityService.createUniversity(universityInput);
        loadUniversities();
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        convertirADto();
        universityDto = universityService.updateUniversity(universityDto.getId(), universityInput);
        loadUniversities();
    }

    @FXML
    void onActionBtnDelete(ActionEvent event) throws Exception {
        universityService.deleteUniversity(universityDto.getId());
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
        ((Stage) universityTable.getScene().getWindow()).close();
    }

    @FXML
    void onMousePressedUniversityTable(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            universityDto = universityTable.getSelectionModel().getSelectedItem();
            convetirainput();
            System.out.println(universityInput);
            bindUniversity(false);
        }
    }

    private void bindUniversity(Boolean newUniversity) {
        if (!newUniversity) {
            idTxf.textProperty().bind(universityInput.id);
        }
        txfName.textProperty().bindBidirectional(universityInput.name);
        txfLocation.textProperty().bindBidirectional(universityInput.location);
    }


    private void convetirainput() {
        this.universityInput.setId(new SimpleStringProperty(universityDto.getId().toString()));
        universityInput.setName(new SimpleStringProperty(universityDto.getName()));
        universityInput.setLocation(new SimpleStringProperty(universityDto.getLocation()));
    }

    private void convertirADto() {
        if (universityInput.getId() != null &&
                !(universityInput.getId().get().equals(universityDto.getId().toString()))) {
            extractId();
        }
        universityDto.setLocation(universityInput.getLocation().get());
        universityDto.setName(universityInput.getName().get());
    }

    void extractId() {
        try {
            universityDto.setId(Long.parseLong(universityInput.getId().get()));
        } catch (NumberFormatException e) {
            System.err.println("Error: No se puede convertir a Long");
        }
    }

}
