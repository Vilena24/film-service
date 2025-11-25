package com.example.film_service.service;

import com.example.film_service.dto.FilmDto;
import com.example.film_service.entity.Film;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class FilmMapperService{

    private final ModelMapper mapper;

    public FilmMapperService(ModelMapper mapper){

        this.mapper = mapper;
    }

    public Film toEntity(FilmDto dto){
        if (dto == null) return null;


        Film film = new Film();
        film.setFilmId(dto.getFilmId());
        film.setFilmName(dto.getNameRu() != null && !dto.getNameRu().isEmpty()
        ? dto.getNameRu()
                : dto.getNameEn());
        film.setReleaseYear(dto.getYear());
        film.setRating(dto.getRating());
        film.setDescription(dto.getDescription());
        return film;

    }

    public FilmDto toDto(Film entity){
        if (entity == null) return null;

        FilmDto dto = new FilmDto();
        dto.setFilmId(entity.getFilmId());
        dto.setNameRu(entity.getFilmName());
        dto.setYear(entity.getReleaseYear());
        dto.setRating(entity.getRating());
        dto.setDescription(entity.getDescription());

        return dto;
    }

}