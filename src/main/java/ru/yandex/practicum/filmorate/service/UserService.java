package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserService implements UserStorage {
    private long currentMaxId = 0L;
    private final Map<Long, User> users;

    @Getter
    InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryStorage inMemoryStorage) {
        this.userStorage = inMemoryStorage.getUserStorage();
        users = userStorage.getUsers();
    }

    public User create(User user) {
        checkUserLogin(user);

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        checkUserEmail(user);
        checkUserBirthday(user);

        user.setId(getNextId());
        userStorage.getUsers().put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        checkUserId(newUser);

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            if (newUser.getLogin() != null) {
                checkUserLogin(newUser);
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                if (!newUser.getEmail().equals(oldUser.getEmail())) {
                    checkUserEmail(newUser);
                }
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getBirthday() != null) {
                checkUserBirthday(newUser);
                oldUser.setBirthday(newUser.getBirthday());
            }
            users.put(oldUser.getId(), oldUser);
            return oldUser;
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public long getNextId() {
        return ++currentMaxId;
    }

    public User getUserById(Long userId) {
        User user = userStorage.getUsers().get(userId);
        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");
        return user;
    }

    public void checkUserId(User user) {
        if (user.getId() == null) {
            throw new ParameterNotValidException("Id должен быть указан");
        }
    }

    public void checkUserLogin(User user) {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ParameterNotValidException("Логин пользователя не может быть пустым");
        }

        if (user.getLogin().contains(" ")) {
            throw new ParameterNotValidException("Логин пользователя не должен содержать пробелы");
        }
    }

    public void checkUserEmail(User user) {
        if (user.getEmail().isEmpty()) {
            throw new ParameterNotValidException("Имейл должен быть указан");
        }

        if (new ArrayList<>(userStorage.getUsers().values()).stream().anyMatch(userInList -> userInList.getEmail().equals(user.getEmail()))) {
            throw new ParameterNotValidException("Этот имейл уже используется");
        }

        if (!user.getEmail().contains("@")) {
            throw new ParameterNotValidException("Некорректный имейл");
        }
    }

    public void checkUserBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ParameterNotValidException("День рождения пользователя не может быть в будущем");
        }
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);

        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        if (friend == null) throw new NotFoundException("Пользователь с id " + friendId + " не найден");

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getUsers().get(userId);

        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        if (userStorage.getUsers().get(friendId) == null) throw new NotFoundException("Пользователь с id " + friendId + " не найден");

        user.getFriends().remove(friendId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.getUsers().get(userId);

        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        ArrayList<User> listFriends = new ArrayList<>();
        for (Long id : user.getFriends()) {
            listFriends.add(userStorage.getUsers().get(id));
        }

        return listFriends;
    }

    public Collection<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        User firstUser = userStorage.getUsers().get(firstUserId);

        if (firstUser == null) throw new NotFoundException("Пользователь с id " + firstUserId + " не найден");

        User secondUser = userStorage.getUsers().get(secondUserId);

        if (secondUser == null) throw new NotFoundException("Пользователь с id " + secondUserId + " не найден");

        Set<Long> commonFriends = new HashSet<>(firstUser.getFriends());
        commonFriends.retainAll(secondUser.getFriends());

        return users.values().stream().filter(user -> commonFriends.contains(user.getId())).collect(Collectors.toList());
    }

}
