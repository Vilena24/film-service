package com.example.film_service.service;

import com.example.film_service.entity.Film;

import java.io.IOException;
import java.util.List;

public interface ReportService {

    byte[] generateCsv(List<Film> films) throws IOException;

    byte[] generateXml(List<Film> films);
    void sendReport(String format, String email) throws IOException;

    void sendReport(String format, String email, byte[] attachment, String filename);
}
