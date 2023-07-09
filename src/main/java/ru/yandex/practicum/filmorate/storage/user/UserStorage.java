package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    Map<Integer, User> getAllUsers();

    User getUserById(Integer id);

    User updateUser(User user);

    Integer removeUser(Integer id);

    User addFriends(Integer id, Integer friendsId);

    Collection<User> getUserFriends(Integer id);

    Collection<User> getMutualFriends(Integer id1, Integer id2);

    Integer removeFriends(Integer id, Integer removeFriendsId);
}
