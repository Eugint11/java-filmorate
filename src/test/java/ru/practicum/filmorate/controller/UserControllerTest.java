package ru.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.practicum.filmorate.typeAdapter.LocalDateTypeAdapter;
import ru.practicum.filmorate.user.User;
import ru.practicum.filmorate.user.UserController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
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
        assertEquals(200, userController.postUser(user).getStatusCode().value());
    }

    @Test
    void postUserWithEmptyNAME() {
        User user = User.builder().id(1).login("login").name("").email("test@gmail.com").birthday(LocalDate.of(1996, 06, 11)).build();
        assertEquals(200, userController.postUser(user).getStatusCode().value());
    }

    @Test
    void postUserWithIncorrect() {
        User user = User.builder()
                .id(1)
                .login("")
                .name("")
                .email("testgmail.com")
                .birthday(LocalDate.of(1996, 06, 11))
                .build();
        ResponseEntity<String> response = userController.postUser(user);
        assertTrue(response.getStatusCode().value() != 200);
    }

    @Test
    void getUsers() {
        User user = User.builder()
                .id(1)
                .login("login")
                .name("")
                .email("test@gmail.com")
                .birthday(LocalDate.of(1996, 06, 11))
                .build();
        userController.postUser(user);
        assertEquals(200, userController.getUsers().getStatusCode().value());
    }

    @Test
    void putUserCorrect() {
        User user = User.builder()
                .id(1).login("login")
                .name("name")
                .email("test@gmail.com")
                .birthday(LocalDate.of(1996, 06, 11))
                .build();
        userController.postUser(user);
        ResponseEntity<String> response = userController.postUser(user);
        assertEquals(200, response.getStatusCode().value());
        user = user.toBuilder().name("newName").build();
        assertEquals(200, userController.putUser(user).getStatusCode().value());
    }

    @Test
    void putUserWithEmptyNAME() {
        User user = User.builder()
                .id(1).login("login")
                .name("name")
                .email("test@gmail.com")
                .birthday(LocalDate.of(1996, 06, 11))
                .build();
        userController.postUser(user);
        User updateUser = user.toBuilder().name("").build();
        assertEquals(200, userController.putUser(updateUser).getStatusCode().value());
    }

    @Test
    void putUserWithIncorrect() {
        User user = User.builder()
                .id(1)
                .login("")
                .name("")
                .email("testgmail.com")
                .birthday(LocalDate.of(1996, 06, 11))
                .build();
        ResponseEntity<String> response = userController.putUser(user);
        assertTrue(response.getStatusCode().value() != 200);
    }
}
