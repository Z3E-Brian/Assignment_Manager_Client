package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

public class UniversityMaintenanceController  extends Controller  {

        @FXML
        private TableColumn<UniversityDto, String> LocationColumn;

        @FXML
        private MFXButton btnBuscar;

        @FXML
        private MFXButton btnEliminar;

        @FXML
        private MFXButton btnGuardar;

        @FXML
        private MFXButton btnNuevo;

        @FXML
        private TableColumn<UniversityDto, Long> idColumn;

        @FXML
        private TableColumn<UniversityDto, String> nameColumn;

        @FXML
        private MFXTextField txfId;

        @FXML
        private MFXTextField txfLocation;

        @FXML
        private MFXTextField txfName;

        @FXML
        private TableView<?> universityTable;

        @FXML
        void onActionBtnBuscar(ActionEvent event) {

        }

        @FXML
        void onActionBtnEliminar(ActionEvent event) {

        }

        @FXML
        void onActionBtnGuardar(ActionEvent event) {

        }

        @FXML
        void onActionBtnNuevo(ActionEvent event) {

        }

        @FXML
        void onKeyPressedTxfId(KeyEvent event) {

        }


    @Override
    public void initialize() {

    }
}
