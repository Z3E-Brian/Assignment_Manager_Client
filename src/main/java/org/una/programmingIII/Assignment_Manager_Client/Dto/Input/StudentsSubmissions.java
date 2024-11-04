package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FileDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentsSubmissions {
public Long id;
public String studentName;
public List<FileInput> files;
public String assignmentTitle;

public LocalDateTime createdAt;
public String feedback;
public Integer grade;
public LocalDateTime reviewedAt;
public Long assignmentId;
public Long reviewedBy;
public Long studentId;
}
