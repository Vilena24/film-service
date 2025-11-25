package com.example.film_service.specification;

import com.example.film_service.entity.Film;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Expression;
import java.math.BigDecimal;


public class FilmSpecifications {
    private FilmSpecifications(){}
    public static Specification<Film> nameContains(String text){
        if (text == null || text.trim().isEmpty()){
            return null;
        }

        String pattern = "%" + text.trim().toLowerCase() + "%";

        return (root, query, cb) -> {
            // чтобы поиск был независим от регистра
            Expression<String> nameField = cb.lower(root.get("filmName"));

            return cb.like((Expression<String>) nameField, pattern);
        };
    }

    public static Specification<Film> YearEq(Integer year){
        if (year == null){
            return null;
        }
        return (root, query, cb) -> {
            return cb.equal(root.get("releaseYear"), year);
        };
    }

    public static Specification<Film> ratingBetween(Double min, Double max){
        //если не заданы мин и макс, фильтровать нечего - возвращаем null
        if (min == null && max == null ){
            return null;
        }

        //если не null конвертируем в BigDecimal
        BigDecimal minBd = (min != null) ? BigDecimal.valueOf(min) : null;
        BigDecimal maxBd = (max != null) ? BigDecimal.valueOf(max) : null;

        return (root, query, cb) -> {

            var ratingPath = root.get("rating").as(BigDecimal.class);


            //оба значения заданы - ищем все фильмы с рейтингом ммежду мин и макс
            if(minBd != null && maxBd != null){
                return cb.between(ratingPath, minBd, maxBd);

            } else if(minBd != null){ //задан только мин, ищем все фильмы с рейтингом >= min
                return cb.greaterThanOrEqualTo(ratingPath, minBd);
            } else{
                return cb.lessThanOrEqualTo(ratingPath, maxBd); //ищем все фильмы с рейтингом <= max
            }
        };
    }
}
