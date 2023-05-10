package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

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
    public Film createFilm(@RequestBody Film film) {
        if (film.getName().isBlank()){
            log.warn("Не указана название фильма");
            throw new ValidationException("Не указана название фильма");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        LocalDate date = LocalDate.of(1895, 12,28);
        if (film.getReleaseDate().isBefore(date)){
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        film.setId(generatorId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateUser(@RequestBody Film film) {
        if (!films.containsKey(film.getId())){
            throw new ValidationException("Фильма с таким ID нет");
        }
        films.put(film.getId(), film);
        return film;
    }

    private int generatorId(){
        return ++idGenerator;
    }
}
