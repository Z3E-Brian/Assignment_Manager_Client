package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.UserDto;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CourseService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "http://localhost:8080/api/courses";


    public CourseService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Answer createCourse(CourseInput courseInput) throws Exception {
        CourseDto courseDto = new CourseDto(courseInput);
        String requestBody = objectMapper.writeValueAsString(courseDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CourseDto courseDtoResult = objectMapper.readValue(response.body(), CourseDto.class);
        CourseInput courseInputResult = new CourseInput(courseDtoResult);

        if (response.statusCode() == 201) {
            return new Answer(true, "The course save", "", "course", courseInputResult);
        } else {
            throw new Exception("Error creating Course: " + response.statusCode());
        }
    }

    public Answer deleteCourse(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting course: " + response.statusCode());
        }
        return new Answer(true, "The course was deleted", "Delete Course");
    }

    public List<CourseDto> getCoursesByCareerId(Long careerId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/getByCareerId/" + careerId))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<CourseDto>>() {
            });
        } else {
            throw new Exception("Error fetching courses: " + response.statusCode());
        }
    }
}
