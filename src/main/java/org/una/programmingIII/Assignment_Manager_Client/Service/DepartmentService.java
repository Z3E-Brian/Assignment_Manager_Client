package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class DepartmentService {
    private static final String BASE_URL = "http://localhost:8080/api/departments";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DepartmentService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public DepartmentDto getById(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), DepartmentDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("Department not found with ID: " + id);
        } else {
            throw new Exception("Error retrieving department: " + response.statusCode());
        }
    }


    public DepartmentDto createDepartment(DepartmentDto departmentDto) throws Exception {
        String requestBody = objectMapper.writeValueAsString(departmentDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), DepartmentDto.class);
        } else {
            throw new Exception("Error creating Department: " + response.statusCode());
        }
    }

    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) throws Exception {
        String requestBody = objectMapper.writeValueAsString(departmentDto);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), DepartmentDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("Department not found");
        } else {
            throw new Exception("Error updating Departmen: " + response.statusCode());
        }
    }

    public void deleteDepartment(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting department: " + response.statusCode());
        }
    }
}

