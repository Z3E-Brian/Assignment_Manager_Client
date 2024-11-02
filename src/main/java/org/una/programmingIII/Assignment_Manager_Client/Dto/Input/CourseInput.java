package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Service.UserService;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInput {
    public Long id;
    public SimpleStringProperty name;
    public SimpleStringProperty description;
    public UserDto professor;
    private Long careerId;
    public ObjectProperty<LocalDate> startDate;
    public ObjectProperty<LocalDate> endDate;

    public CourseInput(CourseDto courseDto) {
        this();
        this.id = courseDto.getId();
        this.name = new SimpleStringProperty(courseDto.getName());
        this.description = new SimpleStringProperty(courseDto.getDescription());
        this.careerId = courseDto.getCareerId();
        this.startDate = new SimpleObjectProperty<>(courseDto.getStartDate());
        this.endDate = new SimpleObjectProperty<>(courseDto.getEndDate());
        this.getProfessor(courseDto);
    }


    private void getProfessor(CourseDto courseDto) {
        try {
            Answer answer = new UserService().getById(courseDto.getProfessorId());
            if (answer.getState()) {
                this.professor = (UserDto) answer.getResult("userDto");
            }
        } catch (Exception e) {
            professor = new UserDto();
        }
    }
}
