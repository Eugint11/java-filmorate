package ru.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.practicum.filmorate.user.User;
import ru.practicum.filmorate.user.UserController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    static UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void postUserCorrect() {
        User user = User.builder().id(1).login("login").name("name").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.postUser(user);
        assertEquals(user, userController.getUsers().getBody().get(0));
    }

    @Test
    void postUserWithEmptyNAME() {
        User user = User.builder().id(1).login("login").name("").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.postUser(user);
        assertEquals("login", userController.getUsers().getBody().get(0).getName());
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
        assertEquals(user, userController.getUsers().getBody().get(0));
    }

    @Test
    void putUserCorrect() {
        User user = User.builder().id(1).login("login").name("name").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.putUser(user);
        assertEquals(user, userController.getUsers().getBody().get(0));
    }

    @Test
    void putUserWithEmptyNAME() {
        User user = User.builder().id(1).login("login").name("").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        userController.putUser(user);
        assertEquals("login", userController.getUsers().getBody().get(0).getName());
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
        assertEquals(updateUser, userController.getUsers().getBody().get(0));
    }
}
