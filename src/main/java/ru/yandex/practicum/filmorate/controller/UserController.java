package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 0;

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Указан некорректный логин {}.", user.getLogin());
            throw new ValidationException("Указан некорректный логин " + user.getLogin() + ".");
        }

        if (user.getName() == null) {
            log.warn("Имя указано пустым. Имени было присвоено значение логина {}.", user.getLogin());
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Неверная дата рождения {}", user.getBirthday());
            throw new ValidationException("Указана не верная дата рождения " + user.getBirthday() + ".");
        }

        user.setId(generatorId());
        users.put(user.getId(), user);
        log.warn("Новый User зарегистрирован.");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким ID нет");
        }
        users.put(user.getId(), user);
        return user;
    }

    private int generatorId() {
        return ++idGenerator;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.warn(errorMessage);
        });
        return errors;
    }
}
