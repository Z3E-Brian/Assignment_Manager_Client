package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
    private boolean isActive;
    private Set<PermissionDto> permissions;
    private Long careerId;
    private List<CourseDto> courses;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

}

