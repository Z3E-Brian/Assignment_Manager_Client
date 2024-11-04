package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    private Long id;
    private String name;
    private String lastName;
    private String secondLastName;
    private String email;
    private String identificationNumber;
    private String password;
    private boolean isActive;
    private Set<PermissionDto> permissions;
    private Long careerId;
    private List<CourseDto> courses;

    public NewUserDto(UserInput userInput) {

        this.id = userInput.getId();
        this.careerId = userInput.getCareerId();

        this.name = userInput.getName();
        this.lastName = userInput.getLastName();
        this.secondLastName = userInput.getSecondLastName();
        this.email = userInput.getEmail().toLowerCase();
        this.password = userInput.getPassword();
        this.identificationNumber = userInput.getIdentificationNumber();
        this.isActive = userInput.getIsActive();
        this.permissions = new HashSet<>();
        this.courses = new ArrayList<>();
    }

}
