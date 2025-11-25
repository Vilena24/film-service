package com.example.film_service.service;


import com.example.film_service.dto.FilmResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class KinopoiskService {

    @Value("${kinopoisk.api.key}")
    private String apiKey;


    private final RestTemplate restTemplate;

    public KinopoiskService(RestTemplate restTemplate){

        this.restTemplate = restTemplate;
    }

    public FilmResponseDto searchFilms(String keyword){
        String url = "https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword?keyword=" + keyword;


        // Заголовки

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        headers.set("Content-Type", "application/json");


        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<FilmResponseDto> response = restTemplate.exchange(url,
                HttpMethod.GET,
                entity,
                FilmResponseDto.class);

        return response.getBody();

    }

}
