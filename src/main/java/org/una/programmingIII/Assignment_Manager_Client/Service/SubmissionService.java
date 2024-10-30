package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CourseInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.SubmissionDto;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SubmissionService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "http://localhost:8080/api/submissions";
    public SubmissionService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Answer createSubmission(SubmissionDto submissionDto) {
        try {
            String requestBody = objectMapper.writeValueAsString(submissionDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            SubmissionDto submissionDtoResult = objectMapper.readValue(response.body(), SubmissionDto.class);

            if (response.statusCode() == 201) {
                return new Answer(true, "The submission save", "", "submission", submissionDtoResult);
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            return new Answer(false, e.getMessage(), "Error to save the submission");
        }
    }
}
