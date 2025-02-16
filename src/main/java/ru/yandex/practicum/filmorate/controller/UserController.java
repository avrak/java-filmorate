package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> users = new HashMap<>();

    public UserController() {
        log.setLevel(Level.TRACE);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.trace("Вывести список пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.trace("Создать пользователя");

        checkUserLogin(user);

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        checkUserEmail(user);
        checkUserBirthday(user);

        user.setId(getNextId());

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.trace("Обновить пользователя");

        if (newUser.getId() == null) {
            String idWarning = "Id должен быть указан";
            log.warn(idWarning);
            throw new ValidationException(idWarning);
        }
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
                log.trace("newUser.getEmail(): " + newUser.getEmail() + "; " + "oldUser.getEmail(): " + oldUser.getEmail());
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
        String userNotFound = "Пользователь с id = " + newUser.getId() + " не найден";
        log.warn(userNotFound);
        throw new NotFoundException(userNotFound);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkUserLogin(User user) {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            String loginEmptyWarning = "Логин пользователя не может быть пустым";
            log.warn(loginEmptyWarning);
            throw new ValidationException(loginEmptyWarning);
        }

        if (user.getLogin().contains(" ")){
            String loginSpacesWarning = "Логин пользователя не должен содержать пробелы: '" + user.getLogin() + "'";
            log.warn(loginSpacesWarning);
            throw new ValidationException(loginSpacesWarning);
        }
    }

    private void checkUserEmail(User user) {
        if (user.getEmail().isEmpty()) {
            String noEmailWarning = "Имейл должен быть указан";
            log.warn(noEmailWarning);
            throw new ValidationException(noEmailWarning);
        }

        if (new ArrayList<>(users.values()).stream().anyMatch(userInList -> userInList.getEmail().equals(user.getEmail()))) {
            String duplicateEmailWarning = "Этот имейл уже используется: '" + user.getEmail() +"'";
            log.warn(duplicateEmailWarning);
            throw new ValidationException(duplicateEmailWarning);
        }

        if (!user.getEmail().contains("@")) {
            String incorrectEmailWarning = "Некорректный имейл: '" + user.getEmail() + "'";
            log.warn(incorrectEmailWarning);
            throw new ValidationException(incorrectEmailWarning);
        }
    }

    private void checkUserBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String incorrectBirthdayWarning = "День рождения пользователя не может быть в будущем: '" + user.getBirthday() + "'";
            log.warn(incorrectBirthdayWarning);
            throw new ValidationException(incorrectBirthdayWarning);
        }
    }
}
