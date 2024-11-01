package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.una.programmingIII.Assignment_Manager_Client.Dto.CareerDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.CareerInput;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CareerService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "http://localhost:8080/api/careers";

    public CareerService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Answer getById(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CareerDto careerDtoResult = objectMapper.readValue(response.body(), CareerDto.class);

        if (response.statusCode() == 200) {
            return new Answer(true, "The career save", "", "careerDto", careerDtoResult);
        } else if (response.statusCode() == 404) {
            return new Answer(false, response.body(), "Error, Career Not Found : " + response.statusCode());
        } else {
            return new Answer(false, response.body(), "Error : " + response.statusCode());
        }
    }


    public Answer createCareer(CareerDto careerDto) throws Exception {
        String requestBody = objectMapper.writeValueAsString(careerDto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        CareerDto careerDtoResult = objectMapper.readValue(response.body(), CareerDto.class);
        CareerInput careerInputResult = new CareerInput(careerDtoResult);

        if (response.statusCode() == 201) {
            return new Answer(true, "The career save", "", "career", careerInputResult);
        } else {
            throw new Exception("Error creating Career: " + response.statusCode());
        }
    }

    public Answer deleteCareer(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new Exception("Error deleting career: " + response.statusCode());
        }
        return new Answer(true, "The career was deleted", "Delete Career");
    }

}