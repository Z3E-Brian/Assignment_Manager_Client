package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FacultyDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityInput {
    private Long id;
    private String name;
    private String location;
    private List<FacultyInput> faculties;

}

