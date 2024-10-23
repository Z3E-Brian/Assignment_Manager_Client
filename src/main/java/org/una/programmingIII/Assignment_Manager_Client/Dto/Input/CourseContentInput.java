package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FileDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseContentInput {
    private Long id;
    private String address;
    private Long courseId;
    private List<FileDto> files;
}
