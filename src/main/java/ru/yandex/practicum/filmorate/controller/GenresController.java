package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Genre;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.*;

@Validated
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresController {
    private final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(GenresController.class);

    private final GenresService genresService;

    @GetMapping
    public Collection<Genre> findAll() {
        log.trace("Вывести список жанров");
        return genresService.findAll();
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable(value = "genreId") Long genreId) {
        return genresService.getGenreById(genreId);
    }
}
