package org.una.programmingIII.Assignment_Manager_Client.Exception;

import lombok.Getter;

@Getter
public class CustomErrorResponse {
    private final String message;
    private final int statusCode;

    public CustomErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}