package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController userController;
    private User user;

    @BeforeEach
    public void create() {
        user = new User(0, "email@mail.ru", "login", "name", LocalDate.of(1986, 3, 21));
    }

    @Test
    public void checkingLogin() {
        user.setLogin(" ");
        ValidationException e = assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals("Указан некорректный логин  .", e.getMessage());
    }

    @Test
    public void checkingEmail() {
        user.setEmail("emailmail.ru");
        ValidationException e = assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals("Некорректный адрес электронной почты emailmail.ru.", e.getMessage());
    }

    @Test
    public void checkingLocalDate() {
        user.setBirthday(LocalDate.of(2024, 3, 21));
        ValidationException e = assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals("Указана не верная дата рождения 2024-03-21.", e.getMessage());
    }
}