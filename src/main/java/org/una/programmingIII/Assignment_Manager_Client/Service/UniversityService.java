package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UniversityDto;
import org.una.programmingIII.Assignment_Manager_Client.Exception.ElementNotFoundException;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class UniversityService {
    private static final String BASE_URL = "http://localhost:8080/api/universities";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
String jwtToken;
    public UniversityService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
    }

    public UniversityDto getUniversityById(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getById?id=" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UniversityDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("University not found");
        } else {
            throw new Exception("Error fetching university: " + response.statusCode());
        }
    }

    public List<UniversityDto> getAllUniversities() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getUniversities"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<UniversityDto>>() {
            });
        } else {
            throw new Exception("Error fetching users: " + response.statusCode());
        }
    }

    // GET: Obtener  Map
    public Map<String, Object> getAllUniversities(int page, int size, int limit) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getMap?page=" + page + "&size=" + size + "&limit=" + limit))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {
            });
        } else {
            throw new Exception("Error fetching universities: " + response.statusCode());
        }
    }

    // POST: Crear un nuevo usuario
    public UniversityDto createUniversity(UniversityDto universityInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(universityInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .header("Authorization", "Bearer " + jwtToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), UniversityDto.class);
        } else {
            throw new Exception("Error creating university: " + response.statusCode());
        }
    }

    // PUT: Actualizar un usuario por ID
    public UniversityDto updateUniversity(Long id, UniversityDto universityInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(universityInput);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), UniversityDto.class);
        } else if (response.statusCode() == 404) {
            throw new ElementNotFoundException("University not found");
        } else {
            throw new Exception("Error updating university: " + response.statusCode());
        }
    }

    public void deleteUniversity(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting university: " + response.statusCode());
        }
    }
}
