package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получин запрос на получение списка всех пользователей.");
        return userService.getAllUsers().values();
    }

    @GetMapping("/{id}")
    public User getUserId(@PathVariable Integer id) {
        log.info("Получин запрос на получение полоьзователя с ID {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получин запрос на создание нового пользователя. ");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получин запрос на обновление пользователя.");
        return userService.updateUser(user);
    }

    @DeleteMapping("/id")
    public Integer removeUser(@PathVariable Integer id) {
        log.info("Получин запрос на удаление пользователя с ID {}.", id);
        return userService.removeUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получин запрос от пользователя с ID {} на добавление в друзья ползователю с  ID {}.", id, friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Integer removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получин запрос от пользователя с ID {} на удаление ползователя с ID {} из зрузей.", id, friendId);
        return userService.removeFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Integer id) {
        log.info("Получин запрос на получение списка друзей рользователя с ID {}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public Collection<User> getFriendFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получин запрос на получение списка общих друзей пользователей с ID {} и {}", id, friendId);
        return userService.getMutualFriends(id, friendId);
    }
}
