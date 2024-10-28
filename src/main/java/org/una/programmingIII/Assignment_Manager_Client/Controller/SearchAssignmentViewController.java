package org.una.programmingIII.Assignment_Manager_Client.Controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager_Client.Service.AssignmentService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;
import org.una.programmingIII.Assignment_Manager_Client.Util.AppContext;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.Format;

import java.time.LocalDate;
import java.util.List;

public class SearchAssignmentViewController extends Controller {
    @FXML
    private MFXButton btnAccept;

    @FXML
    private MFXButton btnSearch;

    @FXML
    private MFXComboBox<AssignmentType> cbxType;

    @FXML
    private MFXDatePicker dtpDueDateAssignment;

    @FXML
    private TableColumn<AssignmentInput, String> tbcDescription;

    @FXML
    private TableColumn<AssignmentInput, LocalDate> tbcDueDate;

    @FXML
    private TableColumn<AssignmentInput, String> tbcTitle;

    @FXML
    private TableColumn<AssignmentInput, String> tbcType;

    @FXML
    private MFXTextField txfDescription;

    @FXML
    private MFXTextField txfTitle;
    @FXML
    private TableView<AssignmentInput> tbvResults;
    @Getter
    Object result;
    private List<AssignmentInput> assignments;

    @FXML
    void onActionBtnAccept(ActionEvent event) {
        result = tbvResults.getSelectionModel().getSelectedItem();
        this.getStage().close();
    }

    @FXML
    void onActionBtnSearch(ActionEvent event) {
        filterList();
    }

    @FXML
    void onMousePressedTbvResults(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            onActionBtnAccept(null);
        }
    }

    @Override
    public void initialize() {
        txfTitle.delegateSetTextFormatter(Format.getInstance().textFormat(150));
        txfDescription.delegateSetTextFormatter(Format.getInstance().textFormat(1000));
        cbxType.getItems().addAll(AssignmentType.values());
        tbcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tbcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tbcType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tbcDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
       // getAssignments();
    }

    private void getAssignments() {
        AssignmentService service = new AssignmentService();
        String position = (String) AppContext.getInstance().get("position");
        Long courseId = (Long) AppContext.getInstance().get("course");
        Answer answer = service.getAllAssignmentsByCourseAndPosition(courseId,position);
        if (answer != null) {
            assignments = (List<AssignmentInput>) answer.getResult("assignments");
            filterList();
        }
    }

    private void filterList() {
        List<AssignmentInput> filteredList = assignments.stream().filter(assignment -> {
            boolean type = cbxType.getSelectionModel().getSelectedItem() == null || assignment.getType().equals(cbxType.getSelectionModel().getSelectedItem());
            boolean title = txfTitle.getText().isBlank() || assignment.getTitle().get().contains(txfTitle.getText());
            boolean description = txfDescription.getText().isBlank() || assignment.getDescription().get().contains(txfDescription.getText());
            boolean dueDate = dtpDueDateAssignment.getValue() == null || assignment.getDueDate().equals(dtpDueDateAssignment.getValue());
            return type && title && description && dueDate;
        }).toList();
        tbvResults.getItems().clear();
        tbvResults.getItems().addAll(filteredList);
    }
}
