package org.una.programmingIII.Assignment_Manager_Client.Controller;

import javafx.fxml.FXML;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserRoleState;
import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;
import org.una.programmingIII.Assignment_Manager_Client.Util.FlowController;

public class AssignmentViewController extends Controller {

    private UserRoleState currentUserRole;

    FlowController flowController = FlowController.getInstance();


    private void setupStudentView() {
    }

    private void setupTeacherView() {
    }
    @Override
    public void initialize() {
        if (currentUserRole == UserRoleState.STUDENT) {
            setupStudentView();
        } else if (currentUserRole == UserRoleState.PROFESSOR) {
            setupTeacherView();
        }
    }

    @FXML
    public void goToUploadFile() {
        FlowController.getInstance().goViewInWindowModal("UploadFileView", this.getStage(), true);
    }

}
