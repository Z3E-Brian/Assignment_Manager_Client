package org.una.programmingIII.Assignment_Manager_Client.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private List<FileDto> files;
    @NotNull(message= "Address cannot be null")
    private String address;
    public AssignmentDto(AssignmentInput assignmentInput) {
        this();
        this.id = assignmentInput.getId();
        this.courseId = assignmentInput.getCourseId();
        this.title = assignmentInput.title.getValue();
        this.type = assignmentInput.getType();
        this.description = assignmentInput.description.getValue();
        this.dueDate = assignmentInput.dueDate.getValue();
        this.address = assignmentInput.getAddress();

    }




}
