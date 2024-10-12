package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDto {
    private Long id;
    private AssignmentDto assignment;
    private UserDto student;
    private String filePath;
    private LocalDateTime createdAt;
    private UserDto reviewedBy;
    private Double grade;
    private String feedback;
    private LocalDateTime reviewedAt;
}
