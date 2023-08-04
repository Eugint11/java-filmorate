package ru.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.practicum.filmorate.LocalDateTypeAdapter;
import ru.practicum.filmorate.user.User;
import ru.practicum.filmorate.user.UserController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    static UserController userController;
    Gson gson;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }

    @Test
    void postUserCorrect() {
        User user = User.builder().id(1).login("login").name("name").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.postUser(user);
        assertEquals(user, userController.getUsers().getBody());
    }

    @Test
    void postUserWithEmptyNAME() {
        User user = User.builder().id(1).login("login").name("").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.postUser(user);
        assertEquals(user, userController.getUsers().getBody());
    }

    @Test
    void postUserWithIncorrect() {
        User user = User.builder().id(1).login("").name("").email("testgmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        ResponseEntity<String> response = userController.postUser(user);
        assertTrue(response.getStatusCode().value() != 200);
    }

    @Test
    void getUsers() {
        User user = User.builder().id(1).login("login").name("").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.postUser(user);
        assertEquals(user, userController.getUsers().getBody());
    }

    @Test
    void putUserCorrect() {
        User user = User.builder().id(1).login("login").name("name").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.putUser(user);
        assertEquals(user, userController.getUsers().getBody());
    }

    @Test
    void putUserWithEmptyNAME() {
        User user = User.builder().id(1).login("login").name("").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.putUser(user);
        assertEquals(user, userController.getUsers().getBody());
    }

    @Test
    void putUserWithIncorrect() {
        User user = User.builder().id(1).login("").name("").email("testgmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        ResponseEntity<String> response = userController.putUser(user);
        assertTrue(response.getStatusCode().value() != 200);
    }

    @Test
    void updateUser() {
        User user = User.builder().id(1).login("login").name("name").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.postUser(user);
        User updateUser = user.toBuilder().name("NewName").build();
        userController.putUser(updateUser);
        assertEquals(user, userController.getUsers().getBody());
    }
}
