package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;

import java.util.Collection;
import java.util.Map;

@Validated
@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(@Valid User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя указано пустым. Имени было присвоено значение логина {}.", user.getLogin());
            user.setName(user.getLogin());
        }
        log.info("Обработка запроса на создание нового пользователя.");
        return userStorage.addUser(user);
    }

    public Map<Integer, User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        if (!userStorage.getAllUsers().containsKey(id)) {
            log.error("Не найден пользователь {}.", id);
            throw new ElementNotFoundException(String.format("Пользователь с ID " + id));
        }
        return userStorage.getUserById(id);
    }

    public User updateUser(@Valid User user) {
        checkUserStorage(user.getId());
        log.info("Обработка запроса на обновление данных пользователя.");
        return userStorage.updateUser(user);
    }

    public Integer removeUser(Integer postId) {
        log.info("Обработка запроса на удаление пользователя");
        return userStorage.removeUser(postId);
    }

    public User addFriends(Integer id, Integer friendId) {
        checkUserStorage(id);
        checkUserStorage(friendId);
        if (userStorage.getUserById(id).getFriends().contains(friendId)) {
            throw new ValidationException("Пользователи уже добавлены в друзья");
        }
        return userStorage.addFriends(id, friendId);
    }

    public Collection<User> getUserFriends(Integer id) {
        checkUserStorage(id);
        return userStorage.getUserFriends(id);
    }

    public Collection<User> getMutualFriends(Integer id1, Integer id2) {
        checkUserStorage(id1);
        checkUserStorage(id2);
        return userStorage.getMutualFriends(id1, id2);
    }

    public Integer removeFriends(Integer id, Integer removeId) {
        checkUserStorage(id);
        checkUserStorage(removeId);
        return userStorage.removeFriends(id, removeId);
    }

    private boolean checkUserStorage(Integer id) {
        if (!userStorage.getAllUsers().containsKey(id)) {
            log.error("Пользователь с ID {} не найден.", id);
            throw new ElementNotFoundException("Пользователь с ID " + id);
        }
        log.warn("Пользователь с ID {} найден.", id);
        return true;
    }


}
