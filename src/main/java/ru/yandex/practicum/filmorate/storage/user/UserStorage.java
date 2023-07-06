package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    Map<Long, User> getAllUsers();

    User getUserById(Long id);

    User updateUser(User user);

    Long removeUser(Long id);

    User addFriends(Long id, Long friendsId);

    Collection<User> getUserFriends(Long id);

    Collection<User> getMutualFriends(Long id1, Long id2);

    Long removeFriends(Long id, Long removeFriendsId);
}
