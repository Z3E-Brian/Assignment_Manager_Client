package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.FileInput;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDto {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private List<FileInput> files;
    private Long reviewedById;
    private Double grade;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}
