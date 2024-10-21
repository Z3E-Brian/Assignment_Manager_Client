package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

public class DepartmentMantainanceController extends Controller {

    @FXML
    private MFXButton btnDelete;

    @FXML
    private MFXButton btnNewDepartment;

    @FXML
    private MFXButton btnSave;

    @FXML
    private TableColumn<?, ?> clmDepartment;

    @FXML
    private TableColumn<?, ?> clmnId;

    @FXML
    private ImageView imvBack;

    @FXML
    private ImageView imvClose;

    @FXML
    private ImageView imvSearch;

    @FXML
    private Label lblFaculty;

    @FXML
    private TableView<?> tbvDepartment;

    @FXML
    private MFXTextField txfDepartmentName;

    @FXML
    private MFXTextField txfId;

    @Override
    public void initialize() {
        System.out.println("Initialize DepartmentMantainanceController");
    }

    @FXML
    void OnMousePressedTbvDepartment(MouseEvent event) {

    }

    @FXML
    void onActionBtnDelete(ActionEvent event) {

    }

    @FXML
    void onActionBtnNewDepartment(ActionEvent event) {

    }

    @FXML
    void onActionBtnSave(ActionEvent event) {

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


}
