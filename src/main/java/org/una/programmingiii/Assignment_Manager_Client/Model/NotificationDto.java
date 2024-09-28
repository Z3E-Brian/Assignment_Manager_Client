package org.una.programmingIII.Assignment_Manager_Client.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private UserDto user;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;
}
