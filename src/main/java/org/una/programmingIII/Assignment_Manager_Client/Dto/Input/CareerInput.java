package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerInput {
    public Long id;
    public SimpleStringProperty name;
    public SimpleStringProperty description;
    public Long departmentId;

    public CareerInput(CareerDto careerDto) {
        this();
        this.name = new SimpleStringProperty(careerDto.getName());
        this.description = new SimpleStringProperty(careerDto.getDescription());
        this.departmentId = careerDto.getDepartmentId();
    }
}
