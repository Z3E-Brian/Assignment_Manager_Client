package org.una.programmingIII.Assignment_Manager_Client.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;
    private Long careerId;
    private String name;
    private String lastName;
    private String secondLastName;
    private String email;
    private String identificationNumber;
    private boolean isActive;
    private Set<PermissionDto> permissions;
    private List<CourseDto> courses;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

    @Override
    public String toString() {
        return name + " " + lastName;
    }

public String getFullName() {
    return (name != null ? name : "") + " " +
           (lastName != null ? lastName : "") + " " +
           (secondLastName != null ? secondLastName : "");
}
}



