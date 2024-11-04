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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class CourseInput implements Serializable {
private static final long serialVersionUID = 1L;

public Long id;
public SimpleStringProperty name;
public SimpleStringProperty description;
public ObjectProperty<UserDto> professor;
public Long careerId;
public List<Long> studentsId;
public ObjectProperty<LocalDate> startDate;
public ObjectProperty<LocalDate> endDate;

public CourseInput() {
    this.name = new SimpleStringProperty("");
    this.description = new SimpleStringProperty("");
    this.professor = new SimpleObjectProperty<>();
    this.startDate = new SimpleObjectProperty<>(LocalDate.now());
    this.endDate = new SimpleObjectProperty<>(LocalDate.now());
}


public CourseInput(CourseDto courseDto) {
    this();
    this.id = courseDto.getId();
    this.name = new SimpleStringProperty(courseDto.getName());
    this.description = new SimpleStringProperty(courseDto.getDescription());
    this.careerId = courseDto.getCareerId();
    this.startDate = new SimpleObjectProperty<>(courseDto.getStartDate());
    this.endDate = new SimpleObjectProperty<>(courseDto.getEndDate());
    this.professor = new SimpleObjectProperty<>(courseDto.getProfessor());
    this.studentsId = courseDto.getStudentsId();
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
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

public UserDto getProfessor() {
    return professor.get();
}

public void setProfessor(UserDto professor) {
    this.professor.set(professor);
}

public Long getCareerId() {
    return careerId;
}

public void setCareerId(Long careerId) {
    this.careerId = careerId;
}

public LocalDate getStartDate() {
    return startDate.get();
}

public void setStartDate(LocalDate startDate) {
    this.startDate.set(startDate);
}

public LocalDate getEndDate() {
    return endDate.get();
}

public void setEndDate(LocalDate endDate) {
    this.endDate.set(endDate);
}

public void setStudentsId(List<Long> studentsId) {
    this.studentsId = studentsId;
}

public List<Long> getStudentsId() {
    return studentsId;
}}