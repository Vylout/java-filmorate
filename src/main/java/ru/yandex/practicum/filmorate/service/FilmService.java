package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Validated
@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Map<Integer, Film> getUsers() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(@Valid Film film) {
        LocalDate date = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(date)) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }

        return filmStorage.addFilm(film);
    }

    public Film updateUser(@Valid Film film) {
        if (!filmStorage.getAllFilms().containsKey(film.getId())) {
            log.error("Фильм с заданным ID не найден.");
            throw new ElementNotFoundException("Фильма " + film.getId());
        }

        return filmStorage.updateFilm(film);
    }

    public Film getFilm(Integer id) {
        if (!filmStorage.getAllFilms().containsKey(id)) {
            log.error("Не найден фильм {}.", id);
            throw new ElementNotFoundException(String.format("Фильм с ID " + id));
        }
        return filmStorage.getFilm(id);
    }

    public Integer addLike(Integer filmId, Integer userId) {
        if (!filmStorage.getAllFilms().containsKey(filmId)) {
            log.error("Фильм с заданным ID не найден.");
            throw new ElementNotFoundException("Фильма " + filmId);
        }
        if (filmStorage.getAllFilms().get(filmId).getLikes().contains(userId)) {
            throw new ValidationException("Лайк уже поставлен.");
        }
        return filmStorage.addLike(filmId, userId);
    }

    public Integer removeLike(Integer filmId, Integer userId) {
        if (!filmStorage.getAllFilms().containsKey(filmId)) {
            log.error("Фильм с заданным ID не найден.");
            throw new ElementNotFoundException("Фильма " + filmId);
        }
        if (!filmStorage.getAllFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь с заданным ID не найден.");
            throw new ElementNotFoundException("Пользователь с ID " + userId);
        }
        return filmStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }
}
