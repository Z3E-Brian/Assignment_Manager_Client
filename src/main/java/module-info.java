module org.una.programmingIII.assignment_manager_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires java.logging;


    opens org.una.programmingIII.Assignment_Manager_Client to javafx.fxml;
    exports org.una.programmingIII.Assignment_Manager_Client;
    exports org.una.programmingIII.Assignment_Manager_Client.Controller;
    opens org.una.programmingIII.Assignment_Manager_Client.Controller to javafx.fxml;
}