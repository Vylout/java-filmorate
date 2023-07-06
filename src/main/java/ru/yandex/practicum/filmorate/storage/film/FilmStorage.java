package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);

    Map<Integer, Film> getAllFilms();

    Film updateFilm(Film film);

    Film getFilm(Integer id);

    Integer addLike(Integer filmId, Integer userId);

    Integer removeLike(Integer filmId, Integer userId);

    Collection<Film> getPopular(Integer size);
}
