package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;

import java.time.LocalDate;
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
    private Long departmentId;
    private LocalDate startDate;
    private LocalDate endDate;
   public CourseDto(CourseInput courseInput){
        this.id = courseInput.id;
        this.name = courseInput.name.getValue();
        this.description = courseInput.description.getValue();
        this.professor = courseInput.getProfessor();
        this.departmentId = courseInput.getDepartmentId();
        this.startDate = courseInput.getStartDate();
        this.endDate = courseInput.getEndDate();

    }
}
