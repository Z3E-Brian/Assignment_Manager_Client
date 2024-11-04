package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.internal.objenesis.instantiator.android.AndroidSerializationInstantiator;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CourseContentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.EmailDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager_Client.Dto.LoginResponse;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;
import org.una.programmingIII.Assignment_Manager_Client.Util.SessionManager;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class AssignmentService {

    private static final String BASE_URL = "http://localhost:8080/api/assignments";  // URL de tu API
    private final ObjectMapper mapper;
    private final String jwtToken;

    public AssignmentService() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        LoginResponse loginResponse = SessionManager.getInstance().getLoginResponse();
        this.jwtToken = loginResponse.getAccessToken();
    }
    public AssignmentDto getAssignmentById(Long id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), AssignmentDto.class);
        } else {
            throw new RuntimeException("Error: " + response.statusCode());
        }
    }

    public Answer getAllAssignmentsByCourseAndPosition(Long courseId, String position) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getByCourseIdAndAddress/" + courseId + "/" + URLEncoder.encode(position, StandardCharsets.UTF_8)))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<AssignmentDto> assignments = mapper.readValue(response.body(), new TypeReference<List<AssignmentDto>>() {});
                List<AssignmentInput> assignmentInputs = assignments.stream().map(AssignmentInput::new).toList();
                return new Answer(true, "", "", "assignments", assignmentInputs);
            } else {
                return new Answer(false, response.body(), "Error: " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("AssignmentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to get the list of assignments");
        }
    }
    public Answer deleteAssignment(Long id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                return new Answer(true, "", "Assignment deleted successfully");
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("AssignmentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to delete the assignment");
        }
    }
    public Answer saveAssignment(AssignmentDto assignmentDto) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String json = mapper.writeValueAsString(assignmentDto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/create"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                AssignmentDto assignmentDtoResult = mapper.readValue(response.body(), AssignmentDto.class);
                return new Answer(true, "", "Assignment created successfully", "assignment", new AssignmentInput(assignmentDtoResult));
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("AssignmentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to create the assignment");
        }
    }
    public Answer getAssignmentsByCourseId(Long courseId) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/getByCourseId/" + courseId))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<AssignmentDto> assignments = mapper.readValue(response.body(), new TypeReference<List<AssignmentDto>>() {});
                return new Answer(true, "", "", "assignments", assignments);
            } else {
                return new Answer(false, response.body(), "Error: " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("AssignmentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to get the list of assignments");
        }
    }
    public Answer sendEmail(EmailDto emailDto){
        try {
            HttpClient client = HttpClient.newHttpClient();
            String json = mapper.writeValueAsString(emailDto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/sendEmail"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new Answer(true, "", "Email sent successfully");
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("AssignmentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to send the email");
        }
    }
}
