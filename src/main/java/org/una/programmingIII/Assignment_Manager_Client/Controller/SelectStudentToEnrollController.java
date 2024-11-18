package org.una.programmingIII.Assignment_Manager_Client.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.*;

import java.util.List;
import java.util.Map;

public class SelectStudentToEnrollController extends Controller {


    @FXML
    private TableColumn<UserDto, Boolean> tbcEnrollCourses;

    @FXML
    private TableColumn<UserDto, String> tbcStudentIdentificationNumber;

    @FXML
    private TableColumn<UserDto, String> tbcStudentName;

    @FXML
    private TableColumn<UserDto, String> tbcStudentLastName;

    @FXML
    private  TableColumn<UserDto, String> tbcStudentSecondLastName;

    @FXML
    private TableView<UserDto> tbvStudents;

    @FXML
    private Pagination pagination;

    private static final int PAGE_SIZE = 10;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    Long careerId;

    @Override
    public void initialize() {
        tbcStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcStudentLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tbcStudentSecondLastName.setCellValueFactory(new PropertyValueFactory<>("secondLastName"));
        tbcStudentIdentificationNumber.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        tbcEnrollCourses.setCellValueFactory((TableColumn.CellDataFeatures<UserDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        tbcEnrollCourses.setCellFactory((TableColumn<UserDto, Boolean> p) -> new ButtonCellEnrollCourse());
        tbvStudents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        loadCareerId();
        configurePagination();
    }

    @FXML
    void onMouseClickedImvBack(MouseEvent event) {
        FlowController.getInstance().delete("SelectStudentToEnrollView");
        FlowController.getInstance().goMain();
    }


    private void loadCareerId() {
        careerId = SessionManager.getInstance().getLoginResponse().getUser().getCareerId();
    }

    private void loadStudents(int pageIndex) {
        try {
            Answer response = new UserService().getStudentsByCareerIdAndPagination(careerId, pageIndex, PAGE_SIZE);
            if (response.getState()) {
                Map<String, Object> rootData = response.getResult();
                Map<String, Object> data = (Map<String, Object>) rootData.get("StudentsByCareerIdAndPagination");
                List<UserDto> students = objectMapper.convertValue(data.get("users"), new TypeReference<>() {
                });
                long totalElements = objectMapper.convertValue(data.get("totalElements"), Long.class);
                tbvStudents.getItems().setAll(students);
                int totalPages = (int) Math.ceil((double) totalElements / PAGE_SIZE);
                pagination.setPageCount(totalPages);
                if (students.isEmpty()) {
                    new Message().showModal(Alert.AlertType.INFORMATION, "Load Students", getStage(), "There are no students enrolled in this career yet.");
                }
            } else {
                throw new Exception(response.getMessage());
            }
        } catch (Exception e) {
            new Message().showModal(Alert.AlertType.ERROR, "Load Students", getStage(), "An Error occurerd while loading students");
        }
    }

    private Node createPage(int pageIndex) {
        loadStudents(pageIndex);
        return new VBox(tbvStudents);
    }

    private void configurePagination() {
        pagination.setPageFactory(this::createPage);
        loadStudents(0);
    }

    private class ButtonCellEnrollCourse extends ButtonCellBase<UserDto> {
        ButtonCellEnrollCourse() {
            super("Enroll", "mfx-btn-Enroll");
        }

        @Override
        protected void handleAction(ActionEvent event) {
            UserDto studentDto = SelectStudentToEnrollController.ButtonCellEnrollCourse.this.getTableView().getItems().get(SelectStudentToEnrollController.ButtonCellEnrollCourse.this.getIndex());
            AppContext.getInstance().set("studentDto", studentDto);
            FlowController.getInstance().deleteAndLoadView("EnrollStudentCourseView");
        }
    }
}
