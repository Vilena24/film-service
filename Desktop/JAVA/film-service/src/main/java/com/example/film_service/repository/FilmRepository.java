package com.example.film_service.repository;

import com.example.film_service.entity.Film;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {
    boolean existsByFilmId(Long filmId);
    Optional<Film> findByFilmId(Long filmId);

    @Override
    List<Film> findAll(Specification<Film> spec);
}
