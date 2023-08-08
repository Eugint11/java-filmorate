package ru.practicum.filmorate.film;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.filmorate.typeAdapter.DurationTypeAdapter;
import ru.practicum.filmorate.typeAdapter.LocalDateTypeAdapter;
import ru.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/films")
public class FilmController {
    private int lastId = 1;
    final int maxLengthDescription = 200;
    final LocalDate minDateRelease = LocalDate.of(1895, 12, 28);
    private List<Film> films = new ArrayList<>();

    @PostMapping
    public ResponseEntity<String> postFilm(@Valid @RequestBody Film film) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
        try {
            validate(film);
            Film newFilm = film.toBuilder().id(getLastId()).build();
            films.add(newFilm);
            log.info("Добавлена запись о фильме: " + newFilm.toString());
            return new ResponseEntity<String>(gson.toJson(newFilm), HttpStatus.OK);
        } catch (ValidationException e) {
            log.error("Возникла ошибка при добавлении фильма. " + e.toString());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<String> getFilms() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
        return new ResponseEntity<String>(gson.toJson(films), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> putFilm(@Valid @RequestBody Film film) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
        try {
            validate(film);
            for (Film oldFilm : films) {
                if (oldFilm.getId() == film.getId()) {
                    films.remove(oldFilm);
                    films.add(film);
                    log.info("Обновлена запись о фильме. Было: " + oldFilm.toString() + ". Стало: " + film.toString());
                    return new ResponseEntity<String>(gson.toJson(film), HttpStatus.OK);
                }
            }
            log.error("Фильм не найден.");
            return new ResponseEntity<String>(gson.toJson("films / Film update unknown"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ValidationException e) {
            log.error("Возникла ошибка при добавлении фильма. " + e.toString());
            return new ResponseEntity<String>(gson.toJson(e.getMessage().toString()), HttpStatus.BAD_REQUEST);
        }
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName().isBlank())
            throw new ValidationException("films / Film create Fail name");
        if (film.getDescription().length() > maxLengthDescription)
            throw new ValidationException("films / Film create Fail description");
        if (film.getReleaseDate().isBefore(minDateRelease))
            throw new ValidationException("films / Film create Fail releaseDate");
        if (film.getDuration() < 0)
            throw new ValidationException("films / Film create Fail duration");
    }

    public int getLastId() {
        return lastId++;
    }
}
