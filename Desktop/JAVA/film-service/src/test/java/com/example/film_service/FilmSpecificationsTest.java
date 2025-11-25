package com.example.film_service;

import com.example.film_service.entity.Film;
import com.example.film_service.repository.FilmRepository;
import com.example.film_service.specification.FilmSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;


import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FilmSpecificationsTest {

    @Autowired
    private FilmRepository filmRepository;

    @BeforeEach
    void setup() {
        filmRepository.deleteAll();

        Film f1 = new Film();
        f1.setFilmId(1L);
        f1.setFilmName("Matrix");
        f1.setReleaseYear(1999);
        f1.setRating(BigDecimal.valueOf(8.7));
        filmRepository.save(f1);

        Film f2 = new Film();
        f2.setFilmId(2L);
        f2.setFilmName("Inception");
        f2.setReleaseYear(2010);
        f2.setRating(BigDecimal.valueOf(8.8));
        filmRepository.save(f2);

        Film f3 = new Film();
        f3.setFilmId(3L);
        f3.setFilmName("Matrix Reloaded");
        f3.setReleaseYear(2003);
        f3.setRating(BigDecimal.valueOf(7.2));
        filmRepository.save(f3);

    }
    @Test
    void testCombinedSpecifications() {
        Specification<Film> spec = FilmSpecifications.nameContains("Matrix");

        Specification<Film> yearSpec = FilmSpecifications.YearEq(1999);
        if (yearSpec != null) spec = (spec == null) ? yearSpec : spec.and(yearSpec);

        Specification<Film> ratingSpec = FilmSpecifications.ratingBetween(8.0, 9.0);
        if (ratingSpec != null) spec = (spec == null) ? ratingSpec : spec.and(ratingSpec);

        List<Film> result = filmRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFilmName()).isEqualTo("Matrix");
    }

    @Test
    void testWhereNullSpecification() {
        Specification<Film> spec = null;
        List<Film> result = filmRepository.findAll(spec); // вернёт все фильмы

        // должны вернуть все фильмы
        assertThat(result).hasSize(3);
    }


}
