package ru.practicum.filmorate.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.filmorate.LocalDateTypeAdapter;
import ru.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private List<User> users = new ArrayList<>();
    private int lastId = 1;

    @PostMapping
    public ResponseEntity<String> postUser(@Valid @RequestBody User user) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        try {
            validate(user);
            User newUser = checkName(user).toBuilder().id(getLastId()).build();
            users.add(newUser);
            return new ResponseEntity<String>(gson.toJson(newUser), HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<String> getUsers() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        return new ResponseEntity<String>(gson.toJson(users), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> putUser(@Valid @RequestBody User user) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        try {
            validate(user);
            for (User oldUser : users) {
                if (oldUser.getId() == user.getId()) {
                    User updateUser = checkName(user);
                    users.add(users.indexOf(oldUser), updateUser);
                    return new ResponseEntity<String>(gson.toJson(updateUser), HttpStatus.OK);
                }
            }
            return new ResponseEntity<String>("", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ValidationException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validate(User user) throws ValidationException {
        if (
                !user.getEmail().contains("@")
                        || user.getLogin().isBlank()
                        || user.getBirthday().isAfter(LocalDate.now())
        ) {
            throw new ValidationException("Некорректно заполнена информация о пользователе");
        }
    }

    private User checkName(User user) {
        return user.getName().isBlank() ? user.toBuilder().name(user.getLogin()).build() : user;
    }

    public int getLastId() {
        return lastId++;
    }
}
