package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CareerInput implements Serializable {

    private static final long serialVersionUID = 1L;
    public SimpleStringProperty id;
    public SimpleStringProperty name;
    public SimpleStringProperty description;
    public SimpleStringProperty departmentId;
    private List<CourseDto> courses;
    private List<Long> usersId;

    public CareerInput() {
        this.id = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.departmentId = new SimpleStringProperty("");
        this.courses = new ArrayList<>();
        this.usersId = new ArrayList<>();
    }

    public CareerInput(CareerDto careerDto) {
        this();
        this.id = new SimpleStringProperty(careerDto.getId().toString());
        this.name = new SimpleStringProperty(careerDto.getName());
        this.description = new SimpleStringProperty(careerDto.getDescription());
        this.departmentId = new SimpleStringProperty(careerDto.getDepartmentId().toString());
        this.courses = careerDto.getCourses();
        this.usersId = careerDto.getUsersId();
    }

    public Long getId() {
        if (this.id.get() != null && !this.id.get().isBlank()) {
            return Long.valueOf(this.id.get());
        }
        return null;
    }

    public void setId(Long id) {
        this.id.set(id.toString());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Long getDepartmentId() {
        if (this.departmentId.get() != null && !this.departmentId.get().isBlank()) {
            return Long.valueOf(this.departmentId.get());
        }
        return null;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId.set(departmentId.toString());
    }
}
