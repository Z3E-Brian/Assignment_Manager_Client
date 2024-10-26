package org.una.programmingIII.Assignment_Manager_Client.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.una.programmingIII.Assignment_Manager_Client.Dto.DepartmentDto;
import org.una.programmingIII.Assignment_Manager_Client.Dto.Input.FileInput;
import org.una.programmingIII.Assignment_Manager_Client.Util.Answer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "http://localhost:8080/api/files";
    private static final String UPLOAD_URL = "http://localhost:8080/files/upload";
    private static final int CHUNK_SIZE = 512 * 1024;

    public FileService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Answer createFile(FileInput fileInput,File file) throws Exception {
        String requestBody = objectMapper.writeValueAsString(fileInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        FileInput fileInputResult = objectMapper.readValue(response.body(), FileInput.class);
        uploadFileInChunks(file,  fileInputResult.getId());

        if (response.statusCode() == 201) {
            return new Answer(true,"The file save","","file", fileInputResult);
        } else {
            throw new Exception("Error creating File: " + response.statusCode());
        }
    }


    public static List<byte[]> splitFile(File file) throws IOException {
        List<byte[]> chunks = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long fileSize = file.length();
            long totalChunks = (long) Math.ceil((double) fileSize / CHUNK_SIZE);

            for (int chunkNumber = 0; chunkNumber < totalChunks; chunkNumber++) {
                long start = chunkNumber * CHUNK_SIZE;
                long chunkSize = Math.min(CHUNK_SIZE, fileSize - start);

                byte[] buffer = new byte[(int) chunkSize];
                raf.seek(start);
                raf.readFully(buffer);
                chunks.add(buffer);
            }
        }
        return chunks;
    } public static void uploadFileInChunks(File file, Long fileInputId) throws IOException {
        List<byte[]> chunks = splitFile(file);
        int totalChunks = chunks.size();
        for (int chunkNumber = 0; chunkNumber < chunks.size(); chunkNumber++) {
            byte[] fileChunk = chunks.get(chunkNumber);
            HttpURLConnection connection = (HttpURLConnection) new URL(UPLOAD_URL).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---Boundary");

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(("-----Boundary\r\nContent-Disposition: form-data; name=\"fileChunk\"; fileId=\"" + fileInputId + "\"\r\nContent-Type: application/octet-stream\r\n\r\n").getBytes());
                outputStream.write(fileChunk);
                outputStream.write(("\r\n-----Boundary\r\nContent-Disposition: form-data; name=\"fileId\"\r\n\r\n" + fileInputId + "\r\n").getBytes());
                outputStream.write(("-----Boundary\r\nContent-Disposition: form-data; name=\"chunkNumber\"\r\n\r\n" + chunkNumber+1 + "\r\n").getBytes());
                outputStream.write(("-----Boundary\r\nContent-Disposition: form-data; name=\"totalChunks\"\r\n\r\n" + totalChunks + "\r\n").getBytes());
                outputStream.write("-----Boundary--\r\n".getBytes());
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Fragment " + chunkNumber + " charge successful.");
            } else {
                System.out.println("Error to charge the fragment " + chunkNumber + ": " + responseCode);
            }
            connection.disconnect();
        }
    }

}
