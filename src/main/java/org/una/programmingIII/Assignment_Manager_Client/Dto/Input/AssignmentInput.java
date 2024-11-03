package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AssignmentInput implements Serializable {
    public Long id;
    public SimpleStringProperty title;
    private ObjectProperty<AssignmentType> type;
    public SimpleStringProperty description;
    public ObjectProperty<LocalDate> dueDate;
    public String address;
    public Long courseId;

    public AssignmentInput() {
        this.id = null;
        this.title = new SimpleStringProperty("");
        this.type = new SimpleObjectProperty<>();
        this.description = new SimpleStringProperty("");
        this.dueDate = new SimpleObjectProperty<>(LocalDate.now());
        this.address = "";
        this.courseId = null;
    }

    public AssignmentInput(AssignmentDto assignmentDto) {
        this();
        this.id = assignmentDto.getId();
        this.title = new SimpleStringProperty(assignmentDto.getTitle());
        this.type.set(assignmentDto.getType());
        this.description = new SimpleStringProperty(assignmentDto.getDescription());
        this.dueDate = new SimpleObjectProperty<>(assignmentDto.getDueDate());
        this.address = assignmentDto.getAddress();
        this.courseId = assignmentDto.getCourseId();
    }


    public ObjectProperty<AssignmentType> typeProperty() {
        return type;
    }

    public AssignmentType getType() {
        return type.get();
    }

    public void setType(AssignmentType type) {
        this.type.set(type);
    }

    @Override
    public String toString() {
        return
                title.getValue() +
              description.getValue()+
             type.get() +
             dueDate.getValue()
                ;
    }
}
