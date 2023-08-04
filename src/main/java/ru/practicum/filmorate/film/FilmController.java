package ru.practicum.filmorate.film;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.filmorate.LocalDateTypeAdapter;
import ru.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/films")
public class FilmController {
    private int lastId = 0;
    final int maxLengthDescription = 200;
    final LocalDate minDateRelease = LocalDate.of(1895, 12, 28);
    private List<Film> films = new ArrayList<>();

    @PostMapping
    public ResponseEntity<String> postFilm(@Valid @RequestBody Film film) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        try {
            validate(film);
            Film newFilm = film.toBuilder().id(getLastId()).build();
            films.add(newFilm);
            log.info("Добавлена запись о фильме: " + film.toString());
            return new ResponseEntity<String>(gson.toJson(film), HttpStatus.OK);
        } catch (ValidationException e) {
            log.error("Возникла ошибка при добавлении фильма. " + e.toString());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        return new ResponseEntity<List<Film>>(films, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> putFilm(@Valid @RequestBody Film film) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        try {
            validate(film);
            for (Film oldFilm : films) {
                if (oldFilm.getId() == film.getId()) {
                    films.add(films.indexOf(oldFilm), film);
                    log.info("Обновлена запись о фильме. Было: " + oldFilm.toString() + ". Стало: " + film.toString());
                    return new ResponseEntity<String>(gson.toJson(film), HttpStatus.OK);
                }
            }
            return postFilm(film);
        } catch (ValidationException e) {
            log.error("Возникла ошибка при добавлении фильма. " + e.toString());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()
                || film.getDescription().length() > maxLengthDescription
                || film.getReleaseDate().isBefore(minDateRelease)
                || film.getDuration().isNegative()) {
            throw new ValidationException("Некорректно заполнена информация о фильме");
        }
    }

    public int getLastId() {
        return ++lastId;
    }
}
