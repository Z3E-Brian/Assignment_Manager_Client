package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseContentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseContentInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

public class CourseContentService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "http://localhost:8080/api/courseContents";
    String jwtToken;
    public CourseContentService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
    }
    public Answer saveCourseContent(CourseContentInput courseContent) {
        try {
            String requestBody = objectMapper.writeValueAsString(courseContent);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return new Answer(true, "", "", "courseContent", objectMapper.readValue(response.body(), CourseContentInput.class));
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("CourseContentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to save the course content");
        }
    }
    public Answer getAllCourseContentById(Long courseId) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getAllByCourseId/" + courseId))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<CourseContentDto> courseContent = objectMapper.readValue(response.body(), new TypeReference<List<CourseContentDto>>() {});
                return new Answer(true, "", "", "courseContent", courseContent);
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("CourseContentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to get the course content");
        }
    }

}
