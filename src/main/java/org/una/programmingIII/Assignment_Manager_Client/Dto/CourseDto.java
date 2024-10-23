package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long id;
    private String name;
    private String description;
    private UserDto professor;
    private List<UserDto> students;
    private DepartmentDto department;
 /*   public CourseDto(CourseInput courseInput){
        this.id = Long.parseLong(courseInput.id.getValue());
        this.name = courseInput.name.getValue();
        this.description = courseInput.description.getValue();
        this.professor = new UserDto(courseInput.professor);
        this.students = courseInput.students;
        this.department = new DepartmentDto(courseInput.department);
    }*/
}
