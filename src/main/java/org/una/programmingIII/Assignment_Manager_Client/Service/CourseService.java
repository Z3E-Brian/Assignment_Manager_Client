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
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;

public class CourseService {

    private static final String BASE_URL = "http://localhost:8080/api/courses";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CourseService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // GET: Obtener todos los usuarios
    public List<CourseDto> getAllCourses() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getAllCourses"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<CourseDto>>() {});
        } else {
            throw new Exception("Error fetching courses: " + response.statusCode());
        }
    }

    // GET: Obtener usuarios paginados en un Map
    public Map<String, Object> getCourses(int page, int size, int limit) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getMap?page=" + page + "&size=" + size + "&limit=" + limit))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
        } else {
            throw new Exception("Error fetching courses: " + response.statusCode());
        }
    }

    // GET: Buscar usuario por email
    public CourseDto getCourseByEmail(String email) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/findByEmail?email=" + email))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), CourseDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("Course not found");
        } else {
            throw new Exception("Error fetching course: " + response.statusCode());
        }
    }

    // POST: Crear un nuevo usuario
    public CourseDto createCourse(CourseInput courseInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(courseInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), CourseDto.class);
        } else {
            throw new Exception("Error creating course: " + response.statusCode());
        }
    }

    // PUT: Actualizar un usuario por ID
    public CourseDto updateCourse(Long id, CourseInput courseInput) throws Exception {
        String requestBody = objectMapper.writeValueAsString(courseInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), CourseDto.class);
        } else if (response.statusCode() == 404) {
            throw new Exception("Course not found");
        } else {
            throw new Exception("Error updating course: " + response.statusCode());
        }
    }

    // DELETE: Eliminar un usuario por ID
    public void deleteCourse(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting course: " + response.statusCode());
        }
    }

//    public boolean authenticate(String coursename, String password) throws Exception {
//        CourseDto courseDto = getCourseByEmail(coursename);
//        if ("admin".equals(coursename) && "admin".equals(password)) {
//            return true;
//        } else {
//            throw new Exception("Invalid credentials");
//        }
//    }
}
