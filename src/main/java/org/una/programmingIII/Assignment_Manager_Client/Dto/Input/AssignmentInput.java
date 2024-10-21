package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;

import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentInput implements Serializable {
    public   SimpleStringProperty id;
    public SimpleStringProperty title;
    public AssignmentType type;
    public SimpleStringProperty description;
    public ObjectProperty<LocalDate> dueDate;
    public CourseInput course;
}
