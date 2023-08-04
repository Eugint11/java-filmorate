package ru.practicum.filmorate.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private List<User> users = new ArrayList<>();
    private int lastId = 0;

    @PostMapping
    public ResponseEntity<String> postUser(@Valid @RequestBody User user) {
        try {
            validate(user);
            User newUser = user.toBuilder().id(getLastId()).build();
            users.add(newUser);
            return new ResponseEntity<String>(newUser.toString(), HttpStatus.OK);
        } catch (ValidationException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping(path = "/users/")
    public ResponseEntity<String> putUser(@Valid @RequestBody User user) {
        try {
            validate(user);
            for (User oldUser : users) {
                if (oldUser.getId() == user.getId()) {
                    users.add(users.indexOf(oldUser), user);
                    return new ResponseEntity<String>(user.toString(), HttpStatus.OK);
                }
            }
            User newUser = user.toBuilder().id(getLastId()).build();
            users.add(newUser);
            return new ResponseEntity<String>(newUser.toString(), HttpStatus.OK);
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

    public int getLastId() {
        return ++lastId;
    }
}
