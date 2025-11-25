package com.example.film_service.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class TestHttpService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TestHttpService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    // используем пакет java.net
    // ответ должен приходить в формате json
    public String testGET() {
        String url = "https://jsonplaceholder.typicode.com/posts?_limit=10"; //создаём объект URL

        String response = restTemplate.getForObject(url, String.class);

        System.out.println("Raw JSON: " + response);

        try{
            JsonNode jsonNode = objectMapper.readTree(response); // парсим JSON через ObjectMapper (из строки в объект)

            String title = jsonNode.get(0).get("title").asText(); // достаем поле Title, берем первый элемент массива JSON и читаем у него заголовок как текст
            System.out.println("First title: " + title);

            return "First title: " + title;

        } catch(final Exception ex) {
            ex.printStackTrace();
             return "GET request did not work";
        }
    }

}
