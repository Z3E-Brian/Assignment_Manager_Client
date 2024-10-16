package org.una.programmingIII.Assignment_Manager_Client.Controller;


import org.una.programmingIII.Assignment_Manager_Client.Dto.UserRoleState;

import org.una.programmingIII.Assignment_Manager_Client.Util.Controller;

public class AssignmentViewController extends Controller {

    private UserRoleState currentUserRole; // Define el rol del usuario (estudiante o profesor)


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


}
