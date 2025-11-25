package com.example.film_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;



@Data
public class FilmResponseDto {
    private String keyword;
    private Integer pagesCount;
    private List<FilmDto> films;


}
