package org.una.programmingIII.Assignment_Manager_Client.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseContentDto {
    private Long id;
    private String address;
    private Long courseId;
   private List<FileDto> files;
}
