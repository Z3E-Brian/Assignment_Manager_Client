package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.internal.objenesis.instantiator.android.AndroidSerializationInstantiator;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AssignmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.AssignmentInput;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class AssignmentService {

    private static final String BASE_URL = "http://localhost:8080/api";  // URL de tu API

    public AssignmentDto getAssignmentById(Long id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/assignments/" + id))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), AssignmentDto.class);
        } else {
            throw new RuntimeException("Error: " + response.statusCode());
        }
    }

    public Answer getAllAssignmentsByCourseAndPosition(Long courseId, String position) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/assignments/getByCourseIdAndAddress/" + courseId + "/" + position))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<AssignmentDto> assignments = List.of(mapper.readValue(response.body(), AssignmentDto[].class));
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
                    .uri(URI.create(BASE_URL + "/assignments/" + id))
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
    public Answer saveAssignment(AssignmentInput assignmentInput) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            AssignmentDto assignmentDtoInput = new AssignmentDto(assignmentInput);
            String json = mapper.writeValueAsString(assignmentDtoInput);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                AssignmentDto assignmentDto = mapper.readValue(response.body(), AssignmentDto.class);
                return new Answer(true, "", "Assignment created successfully", "assignment", new AssignmentInput(assignmentDto));
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
                    .uri(URI.create(BASE_URL + "/assignments/getByCourseId/" + courseId))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                List<AssignmentDto> assignments = List.of(mapper.readValue(response.body(), AssignmentDto[].class));
                return new Answer(true, "", "", "assignments", assignments);
            } else {
                return new Answer(false, response.body(), "Error: " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("AssignmentService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to get the list of assignments");
        }
    }
}
