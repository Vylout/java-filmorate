package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(generatorId());
        films.put(film.getId(), film);
        log.warn("Новый фильм добавлен.");
        return film;
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.warn("Данные о фильме обновлены.");
        return film;
    }

    @Override
    public Film getFilm(Integer id) {
        return films.get(id);
    }

    @Override
    public Integer addLike(Integer filmId, Integer userId) {
        films.get(filmId).getLikes().add(userId);
        return userId;
    }

    @Override
    public Integer removeLike(Integer filmId, Integer userId) {
        films.get(filmId).getLikes().remove(userId);
        return userId;
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        return films.values().stream()
                .sorted(Comparator.comparing(f -> -f.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int generatorId() {
        return id++;
    }
}
