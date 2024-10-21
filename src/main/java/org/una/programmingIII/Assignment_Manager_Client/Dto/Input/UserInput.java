package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {

    private LongProperty id = new SimpleLongProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty identificationNumber = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private List<PermissionType> role;

    public void setPassword(String number) {
        password.set(number);
    }
}
