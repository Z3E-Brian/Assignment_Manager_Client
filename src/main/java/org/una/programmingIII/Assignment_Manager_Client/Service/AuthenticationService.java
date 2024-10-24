package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Exception.InvalidCredentialsException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthenticationService {
    private static final String BASE_URL = "http://localhost:8080/auth";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AuthenticationService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public LoginResponse authenticate(LoginInput loginInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(loginInput);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), LoginResponse.class);
        } else if (response.statusCode() == 404) {
            throw new InvalidCredentialsException("User not found");
        } else if (response.statusCode() == 401) {
            throw new InvalidCredentialsException("Invalid credentials");
        } else {
            throw new Exception("Error founding user: " + response.statusCode());
        }
    }


}
