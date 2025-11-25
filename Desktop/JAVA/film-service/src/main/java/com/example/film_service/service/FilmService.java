package com.example.film_service.service;


import com.example.film_service.dto.FilmDto;
import com.example.film_service.entity.Film;
import com.example.film_service.repository.FilmRepository;
import com.example.film_service.specification.FilmSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapperService filmMapperService;

    public FilmService(FilmRepository filmRepository, FilmMapperService filmMapperService) {
        this.filmRepository = filmRepository;
        this.filmMapperService = filmMapperService;
    }

    public Page<FilmDto> searchFilms(
            String name,
            Integer year,
            Double ratingFrom,
            Double ratingTo,
            int page,
            int size,
            String sortField,
            String sortDir
    ){
        //валидация
        int safePage = Math.max(0,page); // страница не может быть меньше нуля
        int safeSize = Math.min(Math.max(1, size), 50); // размер страницы 1-50

        Double safeRatingFrom = (ratingFrom != null) ? Math.max(0, Math.min(10, ratingFrom)) : null;
        Double safeRatingTo = (ratingTo != null) ? Math.max(0, Math.min(10, ratingTo)) : null;

        Specification<Film> spec = Specification.where(null);

        if (name != null && !name.isBlank()){
            spec = spec.and(FilmSpecifications.nameContains(name));
        }
        if (year != null){
            spec = spec.and(FilmSpecifications.YearEq(year));
        }
        if (safeRatingFrom != null || safeRatingTo!= null){
            spec = spec.and(FilmSpecifications.ratingBetween(safeRatingFrom, safeRatingTo));
        }

        String safeSortField = (sortField == null || sortField.isBlank()) ? "id" : sortField;
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, safeSortField);

        Pageable pageable = PageRequest.of(safePage, safeSize, sort); //номер страницы не отрицательный, размер страницы хотя бы 1

        Page<Film> filmPage = filmRepository.findAll(spec, pageable);

        return filmPage.map(filmMapperService::toDto);
    }

    public List<Film> findLimit(int limit){
        int safeLimit = Math.max(1,(Math.min(1000, limit))); //ограничение

        PageRequest pr = PageRequest.of(0, safeLimit, Sort.by(Sort.Direction.DESC, "id"));
        return filmRepository.findAll(pr).getContent();
    }

    public List<Film> findAll(){
        return filmRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

}
