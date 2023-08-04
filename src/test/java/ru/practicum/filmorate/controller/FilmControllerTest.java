package ru.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.practicum.filmorate.film.Film;
import ru.practicum.filmorate.film.FilmController;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void postFilmCorrected() {
        Film film = Film.builder()
                .id(1)
                .name("Достать баги")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        ResponseEntity<String> response = filmController.postFilm(film);
        assertEquals(response.getStatusCode().value(), 200);
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
        assertEquals(film, filmController.getFilms().getBody().get(0));
    }

    @Test
    void putFilm() {
        Film film = Film.builder()
                .id(1)
                .name("Достать баги")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        ResponseEntity<String> response = filmController.putFilm(film);
        assertEquals(response.getStatusCode().value(), 200);
    }

    @Test
    void putFilmWithIncorrect() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Тестер вышел на охоту в поисках криворукого программиста, который душит код багами")
                .releaseDate(LocalDate.of(2023, 07, 20))
                .duration(Duration.ofMinutes(120)).build();
        ResponseEntity<String> response = filmController.putFilm(film);
        assertTrue(response.getStatusCode().value() != 200);
    }
}
