package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UniversityInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UniversityService;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

public class UniversityMaintainanceFacultiesController extends Controller {

    @FXML
    private MFXButton btnAddFaculty;

    @FXML
    private MFXButton btnDeleteFaculty;

    @FXML
    private MFXButton btnDeparments;

    @FXML
    private Label lblUniversity;

    @FXML
    private ListView<?> lvUniFaculties;

    @FXML
    private MFXTextField txfFacultyName;

    @FXML
    private MFXTextField txfId;

    private UniversityService universityService;
    private UniversityDto universityDto;
    private UniversityInput universityInput;

    @Override
    public void initialize() {
        universityService = new UniversityService();
        universityInput = new UniversityInput();
        universityDto = new UniversityDto();
        updateUniversityInput();
    }


    @FXML
    void onActionBtnDeparments(ActionEvent event) {

    }

    @FXML
    void onActionBtnAddFaculty(ActionEvent event) throws Exception {
        universityDto = universityService.updateUniversity(universityInput.getId(), universityInput);
        convertToInput();
    }

    @FXML
    void onActionBtndeleteFaculty(ActionEvent event) throws Exception {
        universityDto = universityService.updateUniversity(universityInput.getId(), universityInput);
        convertToInput();
    }

    private void updateUniversityInput() {
        universityInput = (UniversityInput) AppContext.getInstance().get("universityInput");
        lblUniversity.setText(universityInput.getName());
    }


    private void convertToInput() {
        this.universityInput.setId(universityDto.getId());
        this.universityInput.setName(universityDto.getName());
        this.universityInput.setLocation(universityDto.getLocation());
    }

}