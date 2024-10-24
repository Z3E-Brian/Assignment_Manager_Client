package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
    private Set<PermissionDto> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
}

