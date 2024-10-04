module org.una.programmingIII.assignment_manager_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires java.logging;
    requires static lombok;
    requires java.net.http;
    requires com.google.gson;
    requires jakarta.validation;
    requires com.fasterxml.jackson.databind;
    requires modelmapper;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens org.una.programmingIII.Assignment_Manager_Client.Model.Input to com.fasterxml.jackson.databind;
    opens org.una.programmingIII.Assignment_Manager_Client.Model to com.fasterxml.jackson.databind, javafx.base;

    opens org.una.programmingIII.Assignment_Manager_Client to javafx.fxml;
    exports org.una.programmingIII.Assignment_Manager_Client;
    exports org.una.programmingIII.Assignment_Manager_Client.Controller;
    opens org.una.programmingIII.Assignment_Manager_Client.Controller to javafx.fxml;
    exports org.una.programmingIII.Assignment_Manager_Client.Util;
    opens org.una.programmingIII.Assignment_Manager_Client.Util to javafx.fxml;
}
