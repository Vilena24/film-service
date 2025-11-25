package com.example.film_service.controller;


import com.example.film_service.dto.FilmResponseDto;
import com.example.film_service.service.KinopoiskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kinopoisk")
public class KinopoiskController {

    private final KinopoiskService kinopoiskService;


    public KinopoiskController(KinopoiskService kinopoiskService) {
        this.kinopoiskService = kinopoiskService;
    }

    @GetMapping("/search")
    public FilmResponseDto search(String keyword){
        return kinopoiskService.searchFilms(keyword);
    }


}


