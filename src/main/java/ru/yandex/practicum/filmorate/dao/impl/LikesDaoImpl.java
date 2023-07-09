package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.LikesDao;

@Repository
@Slf4j
public class LikesDaoImpl implements LikesDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_ADD_LIKE = "INSERT INTO likes(user_id, film_id) VALUES (?, ?)";
    private static final String SQL_REMOVE_LIKE = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

    @Autowired
    public LikesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_ADD_LIKE, userId, filmId);
        log.debug("Ползователь {} поставил лайк фильму {}.", userId, filmId);
        return userId;
    }

    @Override
    public Integer removeLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_REMOVE_LIKE, userId, filmId);
        log.debug("Ползаватель {} удалил свой лайк фильму {}.", userId, filmId);
        return userId;
    }
}
