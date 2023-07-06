package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User addUser(User user) {
        user.setId(generatorId());
        users.put(user.getId(), user);
        log.warn("Новый User зарегистрирован.");
        return user;
    }

    @Override
    public Map<Long, User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.warn("Данные пользователя обновлены.");
        return user;
    }

    @Override
    public Long removeUser(Long id) {
        users.remove(id);
        log.warn("Пользователь с ID {} удален.", id);
        return id;
    }

    @Override
    public User addFriends(Long id, Long friendId) {
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
        log.warn("Пользователи  {} и {} добавлены в друзья.", id, friendId);
        return users.get(friendId);
    }

    @Override
    public Collection<User> getUserFriends(Long id) {
        List<User> friends = new ArrayList<>();
        Set<Long> setFriends = users.get(id).getFriends();
        for (Long friend : setFriends) {
            friends.add(users.get(friend));
        }
        return friends;
    }

    @Override
    public Collection<User> getMutualFriends(Long id1, Long id2) {
        log.warn("Список общих друзей {} и {} составляется", id1, id2);
        Collection<User> friends = new HashSet<>();
        for (Long id : users.get(id1).getFriends()) {
            if (users.get(id2).getFriends().contains(id)) {
                friends.add(users.get(id));
            }
        }
        log.warn("Список общих друзей {} и {} составлен", id1, id2);
        return friends;
    }

    @Override
    public Long removeFriends(Long id, Long removeId) {
        users.get(id).getFriends().remove(removeId);
        users.get(removeId).getFriends().remove(id);
        log.warn("Пользователи удалены {} и {} из друзей.", id, removeId);
        return id;
    }

    private Long generatorId() {
        return id++;
    }
}
