package org.una.programmingIII.Assignment_Manager_Client.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class FileService {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final String UPLOAD_URL = "http://localhost:8080/api/upload";
    private static final int CHUNK_SIZE = 512 * 1024;

    public Answer uploadFile(File file, FileInput fileInput) throws IOException {
        long fileSize = file.length();
        int totalChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int chunkNumber = 1;
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byte[] chunkData = bytesRead == CHUNK_SIZE ? buffer : trimBuffer(buffer, bytesRead);

                sendChunk(chunkData, fileInput, chunkNumber, totalChunks);

                chunkNumber++;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Answer(true, "File uploaded successfully", "upload");
    }

    private void sendChunk(byte[] chunkData, FileInput fileInput, int chunkNumber, int totalChunks) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Map<Object, Object> data = new HashMap<>();
        data.put("fileChunk", chunkData);
        data.put("fileInput", fileInput);
        data.put("chunkNumber", chunkNumber);
        data.put("totalChunks", totalChunks);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(UPLOAD_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error uploading chunk " + chunkNumber + ": " + response.body());
        } else {
            System.out.println("Chunk " + chunkNumber + " uploaded successfully.");
        }
    }

    private byte[] trimBuffer(byte[] buffer, int length) {
        byte[] trimmedBuffer = new byte[length];
        System.arraycopy(buffer, 0, trimmedBuffer, 0, length);
        return trimmedBuffer;
    }}