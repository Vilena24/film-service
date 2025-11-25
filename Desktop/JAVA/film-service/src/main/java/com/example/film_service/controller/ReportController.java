package com.example.film_service.controller;

import com.example.film_service.entity.Film;
import com.example.film_service.service.FilmService;
import com.example.film_service.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/films")
public class ReportController {
    private final FilmService filmService;
    private final ReportService reportService;


    public ReportController(FilmService filmService, ReportService reportService) {
        this.filmService = filmService;
        this.reportService = reportService;

    }

    @GetMapping("/report/csv")
    public ResponseEntity<byte[]> downloadCsv(@RequestParam(defaultValue = "100") int limit,
                                              @RequestParam(defaultValue = "films.csv") String filename) throws IOException {


            List<Film> films = filmService.findLimit(limit);
            byte[] csvBytes = reportService.generateCsv(films);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvBytes);

        }

        @GetMapping("/report/xml")
        public ResponseEntity<byte[]> exportXml() throws IOException{

        List<Film> films = filmService.findAll();
        byte[] xmlBytes = reportService.generateXml(films);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"films.xml\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/xml; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .body(xmlBytes);
        }

        @PostMapping("/report")
        public ResponseEntity<?> sendReport(
                @RequestParam(name = "format") String format,
                @RequestParam(name = "email") String email
        ) throws IOException {
        reportService.sendReport(format, email);
        return ResponseEntity.ok(Map.of("status", "sent", "format", format, "to", email));
        }

    }

