package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInput {
    public Long id;
    public SimpleStringProperty name;
    public SimpleStringProperty description;
    public UserDto professor;
    private Long departmentId;

    public CourseInput(CourseDto courseDto) {
        this();
        this.id = courseDto.getId();
        this.name = new SimpleStringProperty(courseDto.getName());
        this.description = new SimpleStringProperty(courseDto.getDescription());
        this.professor = courseDto.getProfessor();
        this.departmentId = courseDto.getDepartmentId();
    }
}
