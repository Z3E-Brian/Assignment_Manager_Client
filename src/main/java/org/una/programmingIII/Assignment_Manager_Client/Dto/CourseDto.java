package org.una.programmingIII.Assignment_Manager_Client.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDto {

    private Long id;
    private String name;
    private String description;
    private UserDto professor;
    private List<Long> studentsId;
    private Long careerId;
    private LocalDate startDate;
    private LocalDate endDate;

    public CourseDto(CourseInput courseInput) {
        this.id = courseInput.getId();
        this.name = courseInput.getName();
        this.description = courseInput.getDescription();
        this.professor = courseInput.getProfessor();
        this.studentsId = courseInput.getStudentsId();
        this.careerId = courseInput.getCareerId();
        this.startDate = courseInput.getStartDate();
        this.endDate = courseInput.getEndDate();

    }
}
