package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;

import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentInput implements Serializable {
    public SimpleStringProperty id;
    public SimpleStringProperty title;
    public AssignmentType type;
    public SimpleStringProperty description;
    public ObjectProperty<LocalDate> dueDate;
    public String address;
    public Long courseId;

    public AssignmentInput(AssignmentDto assignmentDto){
        this();
        this.id = new SimpleStringProperty(assignmentDto.getId().toString());
        this.title = new SimpleStringProperty(assignmentDto.getTitle());
        this.type = assignmentDto.getType();
        this.description = new SimpleStringProperty(assignmentDto.getDescription());
        this.dueDate = new SimpleObjectProperty<>(assignmentDto.getDueDate());
        this.address = assignmentDto.getAddress();
        this.courseId =assignmentDto.getCourseId();
    }
}
