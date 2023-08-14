package ru.practicum.filmorate.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.filmorate.typeAdapter.LocalDateTypeAdapter;
import ru.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private List<User> users = new ArrayList<>();
    private int lastId = 1;

    @PostMapping
    public ResponseEntity postUser(@Valid @RequestBody User user) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        try {
            validate(user);
            User newUser = user.toBuilder().id(getLastId()).name(user.getName()).build();
            users.add(newUser);
            return new ResponseEntity(gson.toJson(newUser), HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getUsers() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        return new ResponseEntity(gson.toJson(users), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity putUser(@Valid @RequestBody User user) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        try {
            validate(user);
            User updateUser = user.toBuilder().name(user.getName()).build();
            for (User oldUser : users) {
                if (oldUser.getId() == updateUser.getId()) {
                    users.add(updateUser);
                    users.remove(oldUser);
                    return new ResponseEntity(gson.toJson(updateUser), HttpStatus.OK);
                }
            }
            log.error("Пользователь не найден.");
            return new ResponseEntity(gson.toJson("users / User update unknown"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ValidationException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validate(User user) throws ValidationException {
        if (
                user.getName() == null
                        || !user.getEmail().contains("@")
                        || user.getLogin().isBlank()
                        || user.getBirthday().isAfter(LocalDate.now())
        ) {
            throw new ValidationException("Некорректно заполнена информация о пользователе");
        }
    }

    public int getLastId() {
        return lastId++;
    }
}
