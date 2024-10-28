package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseContentInput;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class CourseContentService {
    private static final String BASE_URL = "http://localhost:8080/api";
    public Answer saveCourseContent(CourseContentInput courseContent) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/courseContents"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(courseContent)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return new Answer(true, "", "", "courseContent", new ObjectMapper().readValue(response.body(), CourseContentInput.class));
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("CourseContentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to save the course content");
        }
    }
    public Answer getAllCourseContentById(Long courseId){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/courseContents/getByCourseId/" + courseId))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Answer(true, "", "", "courseContent", new ObjectMapper().readValue(response.body(), CourseContentInput.class));
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("CourseContentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to get the course content");
        }
    }
}
