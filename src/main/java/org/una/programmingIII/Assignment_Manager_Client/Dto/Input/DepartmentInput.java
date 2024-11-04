package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentInput {
    private Long id;
    private String name;
    private Long facultyId;
}
