package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.una.programmingIII.Assignment_Manager_Client.Dto.FacultyDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class FacultyService {
    private static final String BASE_URL = "http://localhost:8080/api/faculties";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
String jwtToken;
    public FacultyService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
    }

    public FacultyDto getFacultyById(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getById?id=" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), FacultyDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("Faculty not found");
        } else {
            throw new Exception("Error fetching Faculty: " + response.statusCode());
        }
    }

    public Map<String, Object> getFacultiesByUniversityId(Long universityId, int page, int size, int limit) throws Exception {

        String url = BASE_URL + "getPageable/" + universityId + "?page=" + page + "&size=" + size + "&limit=" + limit;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {
            });
        } else {
            throw new Exception("Error fetching faculties by university ID: " + response.statusCode());
        }
    }


    public List<FacultyDto> getAllFaculties() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getAllFaculties"))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<FacultyDto>>() {
            });
        } else {
            throw new Exception("Error fetching faculties: " + response.statusCode());
        }
    }

    public Map<String, Object> getFaculties(int page, int size, int limit) throws Exception {
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
            throw new Exception("Error fetching faculties: " + response.statusCode());
        }
    }

    public FacultyDto createFaculty(FacultyDto facultyInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(facultyInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .header("Authorization", "Bearer " + jwtToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), FacultyDto.class);
        } else {
            throw new Exception("Error creating faculty: " + response.statusCode());
        }
    }

    public FacultyDto updateFaculty(Long id, FacultyDto facultyInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(facultyInput);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), FacultyDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("Faculty not found");
        } else {
            throw new Exception("Error updating faculty: " + response.statusCode());
        }
    }

    public void deleteFaculty(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting faculty: " + response.statusCode());
        }
    }
}
