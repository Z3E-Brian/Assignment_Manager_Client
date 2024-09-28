package org.una.programmingIII.Assignment_Manager_Client.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long id;
    private String name;
    private String description;
    private UserDto professor;
    private List<UserDto> students;
    private DepartmentDto department;
}
