package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.Getter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Logger log = (Logger) LoggerFactory.getLogger(FilmController.class);

    @Getter
    FilmService filmService;
    Map<Long, Film> films;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
        films = filmService.getFilms();
        log.setLevel(Level.TRACE);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Вывести список фильмов");
        return new ArrayList<>(films.values());
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") Long filmId) {
        log.info("Вывести фильм с id " + filmId);

        try {
            return filmService.getFilmById(filmId);
        } catch (ParameterNotValidException e) {
            log.warn(e.getReason());
            throw e;
        }
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Сохранить фильм");

        try {
            return filmService.create(film);
        } catch (ParameterNotValidException e) {
            log.warn(e.getReason());
            throw e;
        }
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновить фильм");

        if (newFilm.getId() == null) {
            String idWarning = "Id должен быть указан";
            log.warn(idWarning);
            throw new ParameterNotValidException(idWarning);
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (!newFilm.getName().isEmpty()) {
                oldFilm.setName(newFilm.getName());
            }
            if (!newFilm.getDescription().isEmpty()) {
                try {
                    filmService.validateFilmDescription(newFilm);
                } catch (ParameterNotValidException e) {
                    log.warn(e.getReason());
                    throw e;
                }
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null) {
                try {
                    filmService.validateFilmReleaseDate(newFilm);
                } catch (ParameterNotValidException e) {
                    log.warn(e.getReason());
                    throw e;
                }
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null) {
                try {
                    filmService.validateFilmDuration(newFilm);
                } catch (ParameterNotValidException e) {
                    log.warn(e.getReason());
                    throw e;
                }
                oldFilm.setDuration(newFilm.getDuration());
            }
            films.put(oldFilm.getId(), oldFilm);
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
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

        try {
            filmService.deleteLikeByUserId(filmId, userId);
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        log.info("Вывести " + count + " популярных фильмов");

        return filmService.getPopularFilms(count);
    }
}
