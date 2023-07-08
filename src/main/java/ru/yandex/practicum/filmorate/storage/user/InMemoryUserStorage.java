package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User addUser(User user) {
        user.setId(generatorId());
        users.put(user.getId(), user);
        log.warn("Новый User зарегистрирован.");
        return user;
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.warn("Данные пользователя обновлены.");
        return user;
    }

    @Override
    public Integer removeUser(Integer id) {
        users.remove(id);
        log.warn("Пользователь с ID {} удален.", id);
        return id;
    }

    @Override
    public User addFriends(Integer id, Integer friendId) {
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        log.warn("Пользователи  {} и {} добавлены в друзья.", id, friendId);
        return users.get(friendId);
    }

    @Override
    public Collection<User> getUserFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        Set<Integer> setFriends = users.get(id).getFriends();
        for (Integer friend : setFriends) {
            friends.add(users.get(friend));
        }
        return friends;
    }

    @Override
    public Collection<User> getMutualFriends(Integer id1, Integer id2) {
        log.warn("Список общих друзей {} и {} составляется", id1, id2);
        Collection<User> friends = new HashSet<>();
        for (Integer id : users.get(id1).getFriends()) {
            if (users.get(id2).getFriends().contains(id)) {
                friends.add(users.get(id));
            }
        }
        log.warn("Список общих друзей {} и {} составлен", id1, id2);
        return friends;
    }

    @Override
    public Integer removeFriends(Integer id, Integer removeId) {
        users.get(id).getFriends().remove(removeId);
        users.get(removeId).getFriends().remove(id);
        log.warn("Пользователи удалены {} и {} из друзей.", id, removeId);
        return id;
    }

    private Integer generatorId() {
        return id++;
    }
}
