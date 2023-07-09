package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getUsers() {
        log.info("Получин запрос на получение списка всех фильмов.");
        return filmService.getALLFilms().values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получин запрос на добавление фильма.");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateUser(@RequestBody Film film) {
        log.info("Получин запрос на обновление фильма.");
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Получин запрос на получение фильма с ID {}.", id);
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получин запрос на добавление лайка фильму с ID {} от пользователя с ID {}.", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer removeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получин запрос на удаление лайка фильму с ID {} от пользователя с ID {}.", id, userId);
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получин запрос на получение {} популярных фильмов.", count);
        return filmService.getPopular(count);
    }
}
