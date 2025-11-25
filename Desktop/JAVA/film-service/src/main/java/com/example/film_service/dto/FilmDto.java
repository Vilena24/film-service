package com.example.film_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FilmDto{
    private Long filmId;
    @JsonProperty("nameRu")
    private String nameRu;
    @JsonProperty("nameEn")
    private String nameEn;
    private Integer year;
    private BigDecimal rating;
    private String description;
}
