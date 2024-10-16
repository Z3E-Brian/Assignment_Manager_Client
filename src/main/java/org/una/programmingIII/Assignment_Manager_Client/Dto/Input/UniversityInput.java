package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UniversityInput {
    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public SimpleStringProperty location;
    public List<FacultyInput> faculties;

    public UniversityInput() {
        this.name = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.faculties = new ArrayList<>();
        this.id = new SimpleStringProperty();
    }


}

