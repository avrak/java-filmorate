package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Getter
//    private final InMemoryUserStorage getUserStorage();
    private final InMemoryStorage inMemoryStorage;

    public InMemoryUserStorage getUserStorage() {
        return inMemoryStorage.getUserStorage();
    }
    
    public Map<Long, User> getUsers() {
        return inMemoryStorage.getUserStorage().getAll();
    }

    public User create(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        getUserStorage().create(user);
        return user;
    }

    public User update(User newUser) {
        checkUserId(newUser);

        if (getUsers().containsKey(newUser.getId())) {
            User oldUser = getUsers().get(newUser.getId());

            if (newUser.getLogin() != null) {
                oldUser.setLogin(newUser.getLogin());
            }

            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }

            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            getUserStorage().update(oldUser);
            return oldUser;
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public User getUserById(Long userId) {
        User user = getUserStorage().getUserById(userId);
        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");
        return user;
    }

    public void checkUserId(User user) {
        if (user.getId() == null) {
            throw new ParameterNotValidException("Id должен быть указан");
        }
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getUserStorage().getUserById(userId);
        User friend = getUserStorage().getUserById(friendId);

        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        if (friend == null) throw new NotFoundException("Пользователь с id " + friendId + " не найден");

        getUserStorage().addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = getUserStorage().getUserById(userId);

        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        if (getUserStorage().getUserById(friendId) == null) throw new NotFoundException("Пользователь с id " + friendId + " не найден");

        getUserStorage().deleteFriend(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = getUserStorage().getUserById(userId);

        if (user == null) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        return getUserStorage()
                .getAll()
                .values()
                .stream()
                .filter(friend -> user.getFriends().contains(friend.getId())).collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        User firstUser = getUserStorage().getUserById(firstUserId);

        if (firstUser == null) throw new NotFoundException("Пользователь с id " + firstUserId + " не найден");

        User secondUser = getUserStorage().getUserById(secondUserId);

        if (secondUser == null) throw new NotFoundException("Пользователь с id " + secondUserId + " не найден");

        Set<Long> commonFriends = new HashSet<>(firstUser.getFriends());
        commonFriends.retainAll(secondUser.getFriends());

        return getUsers().values().stream().filter(user -> commonFriends.contains(user.getId())).collect(Collectors.toList());
    }

}
