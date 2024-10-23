package org.una.programmingIII.Assignment_Manager_Client.Dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
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
    private Long courseId;

    @NotNull(message= "Address cannot be null")
    private String address;
    public AssignmentDto(AssignmentInput assignmentInput) {
        this();
        this.id = Long.parseLong(assignmentInput.id.getValue());
        this.title = assignmentInput.title.getValue();
        this.type = assignmentInput.type;
        this.description = assignmentInput.description.getValue();
        this.dueDate = assignmentInput.dueDate.getValue();
        this.address = assignmentInput.address;
        this.courseId = assignmentInput.courseId;
    }

}
