package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Вывести список пользователей");
        return  userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable(value = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.trace("Создать пользователя");

        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        log.trace("Обновить пользователя");

        return userService.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId
    ) {
        log.trace("Создать друзей из " + userId + " и " + friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable("id") Long userId,
            @PathVariable("friendId") Long friendId
    ) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable("id") Long firstUserId,
            @PathVariable("otherId") Long secondUserId
    ) {
        return userService.getCommonFriends(firstUserId, secondUserId);
    }
}
