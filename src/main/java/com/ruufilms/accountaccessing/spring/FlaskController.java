package com.ruufilms.accountaccessing.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruufilms.Beans.ChannelData;
import com.ruufilms.accountaccessing.GroupData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FlaskController {
    private static final String URL = "http://127.0.0.1:5000/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void ping() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "ping").openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = readResponse(connection);
                JsonNode jsonNode = objectMapper.readTree(response);
                String message = jsonNode.get("message").asText();
                String status = jsonNode.get("status").asText();
                if ("success".equals(status)) {
                    System.out.println("Ping Successful: " + message);
                } else {
                    System.out.println("Ping Failed: Internal Server Error");
                }
            } else {
                System.out.println("Ping Failed: HTTP Error Code " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("Error during ping: " + e.getMessage());
        }
    }

    public String createGroup(GroupData groupData) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(groupData);
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "create-group").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                String response = readResponse(connection);
                System.out.println("Create Group Response: " + response);
                return response;
            } else {
                System.out.println("Group Creation Failed: HTTP Error Code " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("Error during group creation: " + e.getMessage());
        }
        return null;
    }

    public String createChannel(ChannelData channelData){
        try{
            String jsonPayload = objectMapper.writeValueAsString(channelData);
            HttpURLConnection connection = (HttpURLConnection) new URL(URL + "create-channel").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                String response = readResponse(connection);
                System.out.println("Create Channel Response: " + response);
                return response;
            } else {
                System.out.println("Group Creation Failed: HTTP Error Code " + responseCode);
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
            return response.toString();
        }
    }

    public static void main(String[] args) {
        FlaskController flaskController = new FlaskController();
        try {
            flaskController.ping();

            GroupData groupData = new GroupData();
            groupData.setGroup_name("Java End Point Test");
            groupData.setUsers(Arrays.asList("user1", "user2"));
            groupData.setPhone_number("+94761964531");

            flaskController.createGroup(groupData);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
