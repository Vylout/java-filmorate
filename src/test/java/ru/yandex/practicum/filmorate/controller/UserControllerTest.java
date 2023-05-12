package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

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
        user.setEmail("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> result = validator.validate(user);
        assertEquals(1, result.size());

        List<String> validationErrors = new ArrayList<>();
        result.forEach(e -> validationErrors.add(e.getMessage()));

        assertTrue(validationErrors.contains("Email не может быть пустым."));
    }

    @Test
    public void checkingEmail2() {
        user.setEmail("emailmail.ru");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> result = validator.validate(user);
        assertEquals(1, result.size());

        List<String> validationErrors = new ArrayList<>();
        result.forEach(e -> validationErrors.add(e.getMessage()));

        assertTrue(validationErrors.contains("Некорректный адрес электронной почты."));
    }

    @Test
    public void checkingLocalDate() {
        user.setBirthday(LocalDate.of(2024, 3, 21));
        ValidationException e = assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals("Указана не верная дата рождения 2024-03-21.", e.getMessage());
    }
}