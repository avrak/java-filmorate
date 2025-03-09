package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {
    public User getUserById(Long userId);

    public long getNextId();

    public void checkUserLogin(User user);

    public void checkUserId(User user);

    public void checkUserEmail(User user);

    public void checkUserBirthday(User user);

    public void addFriend(Long userId, Long friendId);

    public void deleteFriend(Long userId, Long friendId);

    public Collection<User> getFriends(Long userId);

    public Collection<User> getCommonFriends(Long firstUserId, Long secondUserId);
}
