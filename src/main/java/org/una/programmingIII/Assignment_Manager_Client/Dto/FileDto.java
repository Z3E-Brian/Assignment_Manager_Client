package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "toString")
public class FileDto {
    private Long id;
    private String name;
    private Long fileSize;
    private String filePath;
    private Long submissionId;
    private Long assignmentId;
    private Long courseContentId;
    private String provisionalName;

    @Override
    public String toString() {
        return name;
    }
}
