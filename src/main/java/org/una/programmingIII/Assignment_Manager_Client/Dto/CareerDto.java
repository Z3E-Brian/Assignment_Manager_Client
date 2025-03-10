package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CareerInput;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerDto {
    private Long id;
    private String name;
    private String description;
    private Long departmentId;
    private List<Long> coursesId;
    private List<Long> usersId;

    public CareerDto(CareerInput careerInput) {
        this();
        this.id = careerInput.getId();
        this.name = careerInput.getName();
        this.description = careerInput.getDescription();
        this.departmentId = careerInput.getDepartmentId();
        this.coursesId = careerInput.getCoursesId();
        this.usersId = careerInput.getUsersId();
    }
}

