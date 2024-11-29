//package com.ruufilms.accountaccessing.spring;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ruufilms.accountaccessing.GroupData;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.client.RestTemplate;
//
//import java.awt.geom.GeneralPath;
//import java.util.Arrays;
//
//
//public class FlaskController {
//    final static String url = "http://127.0.0.1:5000/";
//    public RestTemplate restTemplate(){
//        return new RestTemplate();
//    }
//    public void Ping() throws JsonProcessingException {
//        String response = restTemplate().getForObject(url+"/ping", String.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(response);
//        String message = jsonNode.get("message").asText();
//        String status = jsonNode.get("status").asText();
//        if(status.equals("success")){
//            System.out.println(message);
//        }else{
//            System.out.println("Internal Server Error");
//        }
//    }
//
//    public void createGroup(GroupData groupData) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonPayload = objectMapper.writeValueAsString(groupData);
//        System.out.println(jsonPayload);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.postForObject(url + "/create-group", entity, String.class);
//        System.out.println(response);
//    }
//    public static void main(String[] args) {
//        FlaskController fc = new FlaskController();
//        try {
//            fc.Ping();
//            GroupData groupData = new GroupData();
//            groupData.setGroup_name("Java End Point Test");
//            groupData.setUsers(Arrays.asList("user1,user,2"));
//            groupData.setPhone_number("+94761964531");
//            fc.createGroup(groupData);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
