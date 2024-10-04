package org.una.programmingIII.Assignment_Manager_Client.Model.Input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.una.programmingIII.Assignment_Manager_Client.Model.PermissionType;

import java.beans.ConstructorProperties;
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

