package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerInput {
    public Long id;
    public SimpleStringProperty name;
    public SimpleStringProperty description;
    public DepartmentDto department;
    public CareerInput(CareerDto careerDto) {
        this();
        this.name = new SimpleStringProperty(careerDto.getName());
        this.description = new SimpleStringProperty(careerDto.getDescription());
        this.department = careerDto.getDepartment();
    }
}
