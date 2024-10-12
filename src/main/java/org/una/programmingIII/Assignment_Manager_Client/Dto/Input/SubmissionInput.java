package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionInput {
    private Long id;
    private AssignmentInput assignment;
    private UserInput student;
    private List<FileInput> files;
    private UserInput reviewedBy;
    private Double grade;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDate reviewedAt;
}
