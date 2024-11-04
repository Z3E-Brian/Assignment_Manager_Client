package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.una.programmingIII.Assignment_Manager_Client.Dto.AnswerAIDto;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

public class AnswerAIService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "http://localhost:8080/api/anwersAI";
    String jwtToken;
    public AnswerAIService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    public Answer getAllAnswerAI() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/"))
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<AnswerAIDto> answerAIDtos = objectMapper.readValue(response.body(), new TypeReference<List<AnswerAIDto>>() {});
                return new Answer(true, "", "", "answersAI", answerAIDtos);
            } else {
                return new Answer(false, response.body(), "Error : " + response.statusCode());
            }
        } catch (Exception e) {
            Logger.getLogger("AnswerAIService").severe(e.getMessage());
            return new Answer(false, e.getMessage(), "Error to get the answersAI");
        }
    }
}
