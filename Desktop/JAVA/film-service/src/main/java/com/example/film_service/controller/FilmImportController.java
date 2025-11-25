package com.example.film_service.controller;


import com.example.film_service.dto.FilmResponseDto;
import com.example.film_service.service.FilmImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v2/films")
public class FilmImportController {

    private FilmImportService filmImportService;

    public FilmImportController(FilmImportService filmImportService){
        this.filmImportService = filmImportService;
    }

    @GetMapping("/external")
    public ResponseEntity<?> importFilms(@RequestParam Map<String, String> params){
        FilmResponseDto response = filmImportService.importFilm(params);
        return ResponseEntity.ok(response.getFilms());
    }
}
