package org.una.programmingIII.Assignment_Manager_Client.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
    private UserRoleState role;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
}

