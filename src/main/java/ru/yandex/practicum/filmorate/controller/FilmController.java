package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Вывести список фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") Long filmId) {
        log.info("Вывести фильм с id " + filmId);

        return filmService.getFilmById(filmId);
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Сохранить фильм");

        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Обновить фильм");

        filmService.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLikeByUserId(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId
    ) {
        log.info("Пользователь с id " + userId + " ставит like фильму с id " + filmId);

        filmService.setLikeByUserId(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeByUserId(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId
    ) {
        log.info("Пользователь с id " + userId + " удаляет like фильму с id " + filmId);

        filmService.deleteLikeByUserId(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", required = false, defaultValue = "10") Long count) {
        log.info("Вывести " + count + " популярных фильмов");

        return filmService.getPopularFilms(count);
    }
}
