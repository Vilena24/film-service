package com.example.film_service.controller;


import com.example.film_service.dto.FilmDto;
import com.example.film_service.service.FilmService;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/films")
public class FilmSearchController {

    private final FilmService filmService;

    public FilmSearchController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public ResponseEntity<Page<FilmDto>> searchFilms(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Double ratingFrom,
            @RequestParam(required = false) Double ratingTo,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ){

        Page<FilmDto> result = filmService.searchFilms(
                name, year, ratingFrom, ratingTo, page, size, sort, dir
        );
                return ResponseEntity.ok(result);
    }
}
