package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentType;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentInput {
    private Long id;
    private String title;
    private AssignmentType type;
    private String description;
    private LocalDate dueDate;
    private CourseInput course;
}
