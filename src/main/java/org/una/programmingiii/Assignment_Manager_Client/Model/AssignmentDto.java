package org.una.programmingIII.Assignment_Manager_Client.Model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Getter
@Setter
public class AssignmentDto {

    private Long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Type cannot be null")
    private AssignmentType type;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @FutureOrPresent(message = "Due date must be in the future or present")
    private LocalDate dueDate;

    @NotNull(message = "Course cannot be null")
    private CourseDto course;


}
