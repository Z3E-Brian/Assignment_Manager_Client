package org.una.programmingIII.Assignment_Manager_Client.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Dto.NewUserDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.UserInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.PermissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;


public class UserService {

    private static final String BASE_URL = "http://localhost:8080/api/users";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private  String jwtToken;
    public UserService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public Answer getById(Long id) throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        UserDto userDtoResult = objectMapper.readValue(response.body(), UserDto.class);

        if (response.statusCode() == 200) {
            return new Answer(true, "The career save", "", "userDto", userDtoResult);
        } else if (response.statusCode() == 404) {
            return new Answer(false, response.body(), "Error, Career Not Found : " + response.statusCode());
        } else {
            return new Answer(false, response.body(), "Error : " + response.statusCode());
        }
    }


    public List<UserDto> getAllUsers() throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getAllUsers"))//TODO: remove "/getAllUsers" from URI
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<UserDto>>() {
            });
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }
    }

    public Answer getAllUsersByPermission(String permission) throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getUsersByPermission?permission=" + permission))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new Answer(true, "", "Users fetched successfully", "users", objectMapper.readValue(response.body(), new TypeReference<List<UserDto>>() {
            }));
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }
    }

    public Answer getAllStudentsByCareerId(Long careerId) throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/students/byCareerId/" + careerId))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new Answer(true, "", "students fetched successfully", "students", objectMapper.readValue(response.body(), new TypeReference<List<UserDto>>() {
            }));
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }
    }

    // GET: Obtener usuarios paginados en un Map
    public Map<String, Object> getUsers(int page, int size, int limit) throws Exception {
        return getStringObjectMap(page, size, limit, httpClient, objectMapper);
    }

    static Map<String, Object> getStringObjectMap(int page, int size, int limit, HttpClient httpClient, ObjectMapper objectMapper) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(UserService.BASE_URL + "/getMap?page=" + page + "&size=" + size + "&limit=" + limit))
                .header("Authorization", "Bearer " + SessionManager.getInstance().getLoginResponse().getAccessToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {
            });
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }
    }

    public UserDto getUserByEmail(String email) throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/findByEmail?email=" + email))
                .header("Authorization", "Bearer " + jwtToken)
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

    public Answer createUser(NewUserDto user) {
        try {
            String requestBody = objectMapper.writeValueAsString(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/create"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return new Answer(true, "", "User created successfully", "user", objectMapper.readValue(response.body(), UserDto.class));
            } else if (response.statusCode() == 401) {
     return new Answer(false, "User already registered, please try with another email.", "Error: " + response.statusCode());
            } else {
                String errorMessage = response.body();
                return new Answer(false, errorMessage, "Error: " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("UserService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to save the user");
        }
    }


    public Answer updateUser(Long id, NewUserDto userInput) {
        try {
            LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
            this.jwtToken = loginResponse.getAccessToken();
            String requestBody = objectMapper.writeValueAsString(userInput);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                UserDto updatedUser = objectMapper.readValue(response.body(), UserDto.class);
                return new Answer(true, "", "User updated successfully", "user", updatedUser);
            }
            if (response.statusCode() == 404) {
                throw new Exception("User not found");
            }
            throw new Exception("Error updating user: " + response.statusCode() + " - " + response.body());
        } catch (JsonProcessingException e) {
            Logger.getLogger("UserService").severe("Error serializing user input: " + e.getMessage());
            return new Answer(false, e.getMessage(), "Error serializing user data");
        } catch (IOException e) {
            Logger.getLogger("UserService").severe("IO error during request: " + e.getMessage());
            return new Answer(false, e.getMessage(), "Error sending the request");
        } catch (InterruptedException e) {
            Logger.getLogger("UserService").severe("Request interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();  // Restores interrupt status
            return new Answer(false, e.getMessage(), "Request interrupted");
        } catch (Exception e) {
            Logger.getLogger("UserService").severe("General error: " + e.getMessage());
            return new Answer(false, e.getMessage(), "Error updating the user");
        }
    }

    public Answer deleteUser(Long id) throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting user: " + response.statusCode());
        }
        return null;
    }

    public UserDto getUserById(Long id) throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + jwtToken)
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UserDto.class);
        } else {
            throw new Exception("Error fetching user: " + response.statusCode());
        }
    }

    public List<PermissionDto> getAllPermissions() throws Exception {
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
        String URL= "http://localhost:8080/api/permissions/";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<PermissionDto>>() {
            });
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }

    }

}
