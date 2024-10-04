package org.una.programmingIII.Assignment_Manager_Client.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.una.programmingIII.Assignment_Manager_Client.Model.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Model.UserDto;

public class UserService {

    private static final String BASE_URL = "http://localhost:8080/api/users";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UserService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // GET: Obtener todos los usuarios
    public List<UserDto> getAllUsers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getAllUsers"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<UserDto>>() {});
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }
    }

    // GET: Obtener usuarios paginados en un Map
    public Map<String, Object> getUsers(int page, int size, int limit) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getMap?page=" + page + "&size=" + size + "&limit=" + limit))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }
    }

    // GET: Buscar usuario por email
    public UserDto getUserByEmail(String email) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/findByEmail?email=" + email))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("User not found");
        } else {
            throw new Exception("Error fetching user: " + response.statusCode());
        }
    }

    // POST: Crear un nuevo usuario
    public UserDto createUser(UserInput userInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(userInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), UserDto.class);
        } else {
            throw new Exception("Error creating user: " + response.statusCode());
        }
    }

    // PUT: Actualizar un usuario por ID
    public UserDto updateUser(Long id, UserInput userInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(userInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("User not found");
        } else {
            throw new Exception("Error updating user: " + response.statusCode());
        }
    }

    // DELETE: Eliminar un usuario por ID
    public void deleteUser(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting user: " + response.statusCode());
        }
    }
}
