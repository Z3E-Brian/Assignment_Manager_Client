package org.una.programmingIII.Assignment_Manager_Client.Dto.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInput {

    private Long id;

    private String name;
    private String email;
    private String identificationNumber;
    private String password;
    private List<PermissionType> role;
}

