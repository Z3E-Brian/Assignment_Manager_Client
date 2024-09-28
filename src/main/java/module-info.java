module org.una.programmingiii.assignment_manager_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires java.logging;


    opens org.una.programmingiii.assignment_manager_client to javafx.fxml;
    exports org.una.programmingiii.assignment_manager_client;
    exports org.una.programmingiii.assignment_manager_client.Controller;
    opens org.una.programmingiii.assignment_manager_client.Controller to javafx.fxml;
}