package org.una.programmingIII.Assignment_Manager_Client.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionDto {
    private Long id;
    private PermissionType name;
}
