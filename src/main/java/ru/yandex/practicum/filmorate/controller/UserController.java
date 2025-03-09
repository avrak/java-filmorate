package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users;
    @Getter
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.users = userService.getUserStorage().getUsers();
        log.setLevel(Level.TRACE);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Вывести список пользователей");
        return users.values();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable(value = "userId") Long userId) {
        try {
            return userService.getUserById(userId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.trace("Создать пользователя");

        try {
            return userService.create(user);
        } catch (ParameterNotValidException e) {
            log.warn(e.getReason());
            throw e;
        }
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.trace("Обновить пользователя");

        try {
            return userService.update(newUser);
        } catch (ParameterNotValidException e) {
            log.warn(e.getReason());
            throw e;
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId
    ) {
        try {
            userService.addFriend(userId, friendId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId
    ) {
        try {
            userService.deleteFriend(userId, friendId);
            userService.deleteFriend(friendId, userId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Long userId) {
        try {
            return userService.getFriends(userId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable("id") Long firstUserId,
            @PathVariable("otherId") Long secondUserId
    ) {
        try {
            return userService.getCommonFriends(firstUserId, secondUserId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}
