package com.example.film_service.service;


import com.example.film_service.dto.FilmDto;
import com.example.film_service.dto.FilmResponseDto;
import com.example.film_service.entity.Film;
import com.example.film_service.exception.BadRequestException;
import com.example.film_service.exception.ExternalApiException;
import com.example.film_service.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmImportService {
    @Value("${kinopoisk.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final FilmRepository filmRepository;

    private final FilmMapperService filmMapperService;

    private static final Logger log = LoggerFactory.getLogger(FilmImportService.class);


    public FilmImportService(RestTemplate restTemplate, FilmRepository filmRepository, FilmMapperService filmMapperService) {
        this.restTemplate = restTemplate;
        this.filmRepository = filmRepository;
        this.filmMapperService = filmMapperService;
    }


    public FilmResponseDto importFilm(Map<String, String> params) {

        validateParams(params);

        String keyword = params.get("keyword");
        String url = "https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword?keyword=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);




        try {
            ResponseEntity<FilmResponseDto> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    entity,
                    FilmResponseDto.class);

            FilmResponseDto body = response.getBody(); // достаем тело ответа

            List<FilmDto> films = body.getFilms(); // достаем из него список фильмов

            List<Film> newFilms = films.stream()
                    .filter(filmDto -> !filmRepository.existsByFilmId(filmDto.getFilmId()))
                    .map(filmMapperService::toEntity)
                    .collect(Collectors.toList());

            List<Film> savedFilms = filmRepository.saveAll(newFilms);


            log.info("Пришло: {}, Сохранено: {}", films.size(), savedFilms.size());


            return response.getBody();  //это уже объект FilmResponseDto,
            //а внутри него есть поле List<FilmDto> films

        } catch (HttpClientErrorException e) {
            log.error("Ошибка клиента при обращении к API: статус {}, тело ответа {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Ошибка клиента при запросе к внешнему API", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (HttpServerErrorException e){
            log.error("Ошибка сервера при обращении к API: статус {}, тело ответа {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Ошибка сервера внешнего API", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (ResourceAccessException e){
            log.error("Ошибка сети/таймаута при обращении к API: {}", e.getMessage());
            throw new ExternalApiException("Ошибка сети или таймаут при обращении к API", null, e.getMessage());
        }
    }

    private void validateParams(Map<String, String> params) {

        String keyword = params.get("keyword");

        if (keyword == null || keyword.isBlank()){
            log.warn("Попытка вызова API без обязательного параметра keyword");
            throw new BadRequestException("Параметр 'keyword' обязателен и не может быть пустым");
        }
    }


}
