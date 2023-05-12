package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 0;

    @GetMapping
    public Collection<Film> getUsers() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        LocalDate date = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(date)) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }

        film.setId(generatorId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateUser(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с таким ID нет");
        }
        films.put(film.getId(), film);
        return film;
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
