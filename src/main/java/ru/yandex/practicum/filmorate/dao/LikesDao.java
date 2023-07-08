package ru.yandex.practicum.filmorate.dao;

public interface LikesDao {
    Integer addLike(Integer FilmId, Integer userId);

    Integer removeLike(Integer FilmId, Integer userId);
}
