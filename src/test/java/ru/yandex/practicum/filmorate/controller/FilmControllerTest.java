package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    private FilmController filmController;
    private Film film;

    @BeforeEach
    public void create() {
        film = new Film(0, "name", "description", LocalDate.of(2015, 6, 15), 100);
    }

    @Test
    public void checkingMane() {
        film.setName(" ");
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals("Не указана название фильма.", e.getMessage());
    }

    @Test
    public void checkingDescription() {
        film.setDescription("s".repeat(201));
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals("Максимальная длина описания - 200 символов.", e.getMessage());
    }

    @Test
    public void checkingLocalDate() {
        film.setReleaseDate(LocalDate.of(1894, 12, 28));
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", e.getMessage());
    }

    @Test
    public void checkingDuration() {
        film.setDuration(-2);
        ValidationException e = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e.getMessage());
    }
}