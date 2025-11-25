package com.example.film_service.service;

import com.example.film_service.entity.Film;
import com.example.film_service.repository.FilmRepository;
import com.opencsv.CSVWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.util.ByteArrayDataSource;

import jakarta.mail.internet.MimeMessage;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class ReportServiceImpl implements ReportService {

    private final FilmRepository filmRepository;
    private final JavaMailSender mailSender;

    public ReportServiceImpl(FilmRepository filmRepository, JavaMailSender mailSender) {
        this.filmRepository = filmRepository;
        this.mailSender = mailSender;
    }

    @Override
    public byte[] generateCsv(List<Film> films) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // BOM UTF-8
            out.write(0xEF);
            out.write(0xBB);
            out.write(0xBF);


            try (OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                 CSVWriter csvWriter = new CSVWriter(
                         writer,
                         ';',
                         CSVWriter.DEFAULT_QUOTE_CHARACTER,
                         CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                         CSVWriter.DEFAULT_LINE_END
                 )) {

                csvWriter.writeNext(new String[]{
                        "id", "film_name", "release_year", "rating"
                });

                for (Film film : films) {
                    csvWriter.writeNext(new String[]{
                            String.valueOf(film.getId()),
                            film.getFilmName(),
                            String.valueOf(film.getReleaseYear()),
                            String.valueOf(film.getRating())
                    });
                }

                csvWriter.flush();
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации CSV", e);
        }
    }

    @Override
    public byte[] generateXml(List<Film> films) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<films>\n");


            for (Film film : films){
                xml.append("  <film>\n");

                xml.append("    <id>").append(film.getId()).append("</id>\n");
                xml.append("    <name>").append(escape(film.getFilmName())).append("</name>\n");
                xml.append("    <year>").append(film.getReleaseYear()).append("</year>\n");
                xml.append("    <rating>").append(film.getRating()).append("</rating>\n");

                xml.append("  </film>\n");
            }

            xml.append("</films>");

            out.write(xml.toString().getBytes(StandardCharsets.UTF_8));
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации XML", e);
        }
    }


    @Override
    public void sendReport(String format, String email) throws IOException {
      if (format == null || email == null || email.isBlank() || !email.contains("@")){
          throw new IllegalArgumentException("Неверный email");
      }

      String f = format.trim().toLowerCase();
      byte[] attachment;
      String filename;

      // берем все фильмы
        List<Film> films = filmRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        switch (f){
            case "csv":
                attachment = generateCsv(films);
                filename = "films.csv";
                break;
            case "xml":
                attachment = generateXml(films);
                filename = "films.xml";
                break;
            default:
                throw new IllegalArgumentException("Неподдерживающий формат: " + format + ". Поддерживается csv или xml");
        }
        // отправляем вложение
        sendReport(format, email, attachment, filename);
    }

    @Override
    public void sendReport(String format, String email, byte[] attachment, String filename) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Отчет по фильмам");
            helper.setText("Ваш отчет во вложении (" + format + ")");


            String contentType = format.equalsIgnoreCase("csv")
                    ? "text/csv; charset=UTF-8"
                    : "application/xml; charset=UTF-8";

            helper.addAttachment(filename, new ByteArrayDataSource(attachment, contentType));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке письма", e);
        }
    }

    private String escape(String s){
        if(s == null) return "";
        return s
                .replace("&", "&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;")
                .replace("\"","&quot;")
                .replace("'","&apos;");
    }

}