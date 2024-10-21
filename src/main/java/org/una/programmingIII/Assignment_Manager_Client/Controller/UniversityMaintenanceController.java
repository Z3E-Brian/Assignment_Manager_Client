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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

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

    @FXML
    private ImageView imvClose;

    @FXML
    private ImageView imvSearch;

    @FXML
    private ImageView back;

    private UniversityService universityService;
    private UniversityDto universityDto;
    List<Node> requeridos;

    @Override
    public void initialize() {
        requeridos = new ArrayList<>();
        universityService = new UniversityService();
        universityDto = new UniversityDto();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        universityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        indicateRequeridos();
        loadUniversities();
        idTxf.setDisable(true);
    }

    @FXML
    void onActionBtnNew(ActionEvent event) throws Exception {
        clean();
    }

    @FXML
    void onActionBtnEditFaculties(ActionEvent event) {
        if (universityDto != null && universityTable.getSelectionModel().getSelectedItem() != null) {
            setUniversityDto();
            FlowController.getInstance().goViewInWindow("EditUniversityFacultiesView");
            ((Stage) btnEdit.getScene().getWindow()).close();
        } else {
            new Message().showModal(Alert.AlertType.ERROR, "FacultadesUniversidad", getStage(), "Debes de seleccionar una universidad");

        }
    }

    @FXML
    void onActionBtnSave(ActionEvent event) throws Exception {
        if (!(idTxf.getText().isBlank())) {
            updateUniversity();
        } else {
            createUniversity();
        }
    }

    private void updateUniversity() throws Exception {
        universityDto.setLocation(txfLocation.getText());
        universityDto.setName(txfName.getText());
        universityDto = universityService.updateUniversity(universityDto.getId(), universityDto);
        loadUniversities();
    }


    @FXML
    void onActionBtnDelete(ActionEvent event) throws Exception {
        if (!(idTxf.getText().isEmpty())) {
            universityService.deleteUniversity(universityDto.getId());
            clean();
            loadUniversities();
        } else {
            new Message().showModal(Alert.AlertType.WARNING, "Eliminar universidad", getStage(), "Debe selecionar una de las universidades en la tabla para poder eliminarla.");
        }
    }


    @FXML
    void onMousePressedUniversityTable(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
            universityDto = universityTable.getSelectionModel().getSelectedItem();
            bindear();
            System.out.println(universityDto);
        }
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().goViewInWindow("LogInView");
        ((Stage) btnEdit.getScene().getWindow()).close();
    }

    @FXML
    void onMouseClickedImvSearch(MouseEvent event) {
        System.out.println("search");
    }

    @FXML
    void onMouseClickedImvClose(MouseEvent event) {
        ((Stage) universityTable.getScene().getWindow()).close();
    }

    private void clean() {
        this.idTxf.clear();
        this.txfLocation.clear();
        this.txfName.clear();
        universityDto = new UniversityDto();
    }

    private void createUniversity() throws Exception {
        String invalids = validarRequeridos();
        if (!(invalids.isBlank())) {
            System.out.println(invalids);
            new Message().showModal(Alert.AlertType.ERROR, "Crear universidad", getStage(), "Existen espacios  importantes en blanco");
        } else {
            universityDto = new UniversityDto();
            universityDto.setName(txfName.getText());
            universityDto.setLocation(txfLocation.getText());
            universityService.createUniversity(universityDto);
            clean();
            loadUniversities();
        }
    }

    private void loadUniversities() {
        try {
            List<UniversityDto> universityDtoList = universityService.getAllUniversities();
            ObservableList<UniversityDto> universityDtoObservableList = FXCollections.observableArrayList(universityDtoList);
            universityTable.setItems(universityDtoObservableList);
        } catch (java.net.ConnectException e) {
            new Message().showModal(Alert.AlertType.ERROR, "Connection Error", getStage(), "Failed to connect to the server. Please make sure the server is running.");
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, " Error loading Univesities", getStage(), "Failed to load Universities.");

        }
    }

    private void bindear() {
        txfLocation.setText(universityDto.getLocation());
        txfName.setText(universityDto.getName());
        idTxf.setText(universityDto.getId().toString());
    }

    private void indicateRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txfLocation, txfName));
    }

    private void setUniversityDto() {
        AppContext.getInstance().set("universityDto", universityDto);
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
