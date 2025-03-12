package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Map;

public interface UserStorage {
    public User getUserById(Long userId);

    public Map<Long, User> getAll();

    public User create(User user);

    public User update(User user);

    public void delete(Long userId);

    public void addFriend(Long userId, Long friendId);

    public void deleteFriend(Long userId, Long friendId);

    public long getNextId();
}
