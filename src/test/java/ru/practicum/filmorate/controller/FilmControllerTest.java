package ru.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.practicum.filmorate.LocalDateTypeAdapter;
import ru.practicum.filmorate.film.Film;
import ru.practicum.filmorate.film.FilmController;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController;
    Gson gson;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }

    @Test
    void postFilmCorrected() {
        Film film = Film.builder()
                .id(1)
                .name("Достать баги")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        assertEquals(filmController.postFilm(film).getStatusCode().value(), 200);
    }

    @Test
    void postFilmWithIncorrect() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        ResponseEntity<String> response = filmController.postFilm(film);
        assertTrue(response.getStatusCode().value() != 200);
    }

    @Test
    void getFilms() {
        Film film = Film.builder()
                .id(1)
                .name("Достать баги")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        filmController.postFilm(film);
        assertEquals(200, filmController.getFilms().getStatusCode().value());
    }

    @Test
    void putFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Достать баги")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        assertEquals(filmController.putFilm(film).getStatusCode().value(), 200);
    }

    @Test
    void putFilmWithIncorrect() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        assertTrue(filmController.putFilm(film).getStatusCode().value()!=200);
    }
}
