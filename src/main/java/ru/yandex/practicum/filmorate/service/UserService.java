package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        userRepository.save(user);
        return user;
    }

    public User update(User newUser) {
        checkUserId(newUser);

        Optional<User> oldUser = userRepository.findById(newUser.getId());

        if (oldUser.isPresent()) {

            if (newUser.getLogin() != null) {
                oldUser.get().setLogin(newUser.getLogin());
            }

            if (newUser.getName() != null) {
                oldUser.get().setName(newUser.getName());
            }

            if (newUser.getEmail() != null) {
                oldUser.get().setEmail(newUser.getEmail());
            }

            if (newUser.getBirthday() != null) {
                oldUser.get().setBirthday(newUser.getBirthday());
            }

            userRepository.update(oldUser.get());
            return oldUser.get();
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        return user.get();
    }

    public void checkUserId(User user) {
        if (user.getId() == null) {
            throw new ParameterNotValidException("Id должен быть указан");
        }
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId))  throw new ParameterNotValidException("Друзья совпали с id " + userId);

        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        if (userRepository.findById(friendId).isEmpty()) throw new NotFoundException("Пользователь с id " + friendId + " не найден");

        userRepository.saveFriends(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("Пользователь с id " + userId + " не найден");

        if (userRepository.findById(friendId).isEmpty()) throw new NotFoundException("Пользователь с id " + friendId + " не найден");

        userRepository.deleteFriends(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        if (userRepository.findById(userId).isEmpty())  throw new NotFoundException("Пользователь с id " + userId + " не найден");

        return userRepository.findFriendsByUserId(userId);
    }

    public Collection<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        if (userRepository.findById(firstUserId).isEmpty())  throw new NotFoundException("Пользователь с id " + firstUserId + " не найден");

        if (userRepository.findById(secondUserId).isEmpty())  throw new NotFoundException("Пользователь с id " + secondUserId + " не найден");

        return userRepository.findCommonFriendsByUserId(firstUserId, secondUserId);
    }

}
