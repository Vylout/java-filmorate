package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikesDao likesDao;

    private static final String SQL_GET_FILMS = "SELECT film_id, name, description, release_date," +
            " duration, mpa  FROM films";
    private static final String SQL_GET_LIKES = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String SQL_GET_GENRES = "SELECT genre_id FROM film_genre WHERE " +
            "film_id = ?";
    private static final String SQL_ADD_FILM = "INSERT INTO films(name, description, " +
            "release_date, duration, mpa) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_GET_FILM_ID = "SELECT film_id FROM films WHERE name = ? AND " +
            "description = ? AND release_date = ? AND duration = ? AND mpa = ?";
    private static final String SQL_ADD_GENRE = "INSERT INTO film_genre(film_id, genre_id) " +
            "VALUES (?, ?)";
    private static final String SQL_UPDATE_FILM = "UPDATE films SET name = ?, description = ?, " +
            "release_date = ?, " +
            "duration = ?, mpa = ? WHERE film_id = ?";
    private static final String SQL_DELETE_GENRE = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String SQL_UPDATE_GENRE = "INSERT INTO film_genre(film_id, genre_id) " +
            "VALUES(?, ?)";
    private static final String SQL_GET_FILM = "SELECT * FROM films AS f LEFT JOIN likes AS l " +
            "ON f.film_id = l.film_id WHERE f.film_id = ? GROUP BY f.film_id, l.likes_id";

    private static final String SQL_POPULAR_FILM = "SELECT f.film_id AS id, f.name, f.description, " +
            "f.release_date, f.duration AS duration, f.mpa AS mpa " +
            "FROM films AS f " +
            "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
            "LEFT JOIN film_genre AS fg ON fg.film_id = f.film_id " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(DISTINCT l.user_id) DESC " +
            "LIMIT ? ";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, LikesDao likesDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.likesDao = likesDao;
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        Map<Integer, Film> filmMap = new HashMap<>();
        List<Film> filmList = jdbcTemplate.query(SQL_GET_FILMS, (rs, rowNum) -> makeFilm(rs));
        for (Film f : filmList) {
            filmMap.put(f.getId(), f);
        }
        log.debug("Запрошен список всех фильмов.");
        return filmMap;
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(SQL_ADD_FILM, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        log.debug("Добавлен новый фильм {}.", film);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(SQL_GET_FILM_ID, film.getName(),
                film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId());
        if (filmRows.next()) {
            if (film.getGenres() == null) {
                return new Film(filmRows.getInt("film_id"), film.getName(),
                        film.getDescription(), film.getReleaseDate(), film.getDuration(),
                        film.getMpa(), new HashSet<>(), null);
            } else {
                List<Genre> listGenre = new ArrayList<>(film.getGenres());
                updateGenre(listGenre, filmRows);
                return new Film(filmRows.getInt("film_id"), film.getName(),
                        film.getDescription(), film.getReleaseDate(), film.getDuration(),
                        film.getMpa(), film.getGenres());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(SQL_UPDATE_FILM, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        log.debug("Обновлен фильм {}.", film.getId());
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            jdbcTemplate.update(SQL_DELETE_GENRE, film.getId());
            return film;
        }
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(SQL_DELETE_GENRE, film.getId());
        }
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(SQL_UPDATE_GENRE, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film getFilm(Integer id) {
        log.debug("Получен фильм {}.", id);
        return jdbcTemplate.query(SQL_GET_FILM, (rs, rowNum) -> makeFilm(rs), id).get(0);
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        return jdbcTemplate.query(SQL_POPULAR_FILM, (rs, rowNum) -> makeFilm(rs), count);
    }


    @Override
    public Integer addLike(Integer filmId, Integer userId) {
        return likesDao.addLike(filmId, userId);
    }

    @Override
    public Integer removeLike(Integer filmId, Integer userId) {
        return likesDao.removeLike(filmId, userId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        int mpa = rs.getInt("mpa");
        Set<Integer> likes = new HashSet<>(jdbcTemplate.query(SQL_GET_LIKES, (rs1, rowNum1) ->
                (rs1.getInt("user_id")), id));
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(SQL_GET_GENRES, (rs2, rowNum) ->
                (new Genre(rs2.getInt("genre_id"))), id));
        if (genres.isEmpty()) {
            return new Film(id, name, description, releaseDate, duration, new Mpa(mpa), likes, new HashSet<>());
        }
        return new Film(id, name, description, releaseDate, duration, new Mpa(mpa), likes, genres);
    }

    private SqlRowSet updateGenre(List<Genre> genres, SqlRowSet filmRows) {
        List<Genre> newGenres = genres;
        for (Genre genre : newGenres) {
            jdbcTemplate.update(SQL_ADD_GENRE, filmRows.getInt("film_id"),
                    genre.getId());
        }
        return filmRows;
    }
}