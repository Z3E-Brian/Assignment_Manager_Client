package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInput {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private UserInput professor;
    private List<UserInput> students;
    private DepartmentInput department;
    public CourseInput(CourseDto courseDto){
        this();
        this.id = new SimpleStringProperty(courseDto.getId().toString());
        this.name = new SimpleStringProperty(courseDto.getName());
        this.description = new SimpleStringProperty(courseDto.getDescription());
      /*  this.professor = new UserInput(courseDto.getProfessor());
        this.students = courseDto.getStudents();
        this.department = courseDto.getDepartment();*/
    }
}
