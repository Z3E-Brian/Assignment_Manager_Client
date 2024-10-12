package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;
    private String name;
    private Long facultyId;
}
