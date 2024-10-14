package org.una.programmingIII.Assignment_Manager_Client.Controller;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

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
    private TableColumn<?, ?> idColumn;

    @FXML
    private MFXTextField idTxf;

    @FXML
    private TableColumn<?, ?> locationColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private MFXTextField txfLocation;

    @FXML
    private MFXTextField txfName;

    @FXML
    private TableView<?> universityTable;

    @Override
    public void initialize() {

    }

    @FXML
    void onActionBtnEditFaculties(ActionEvent event) {

    }

    @FXML
    void onActionBtnNew(ActionEvent event) {

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {

    }


}
