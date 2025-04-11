package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Genre;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.*;

@Validated
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenresController {
    private final GenresService genresService;

    @GetMapping
    public Collection<Genre> findAll() {
        log.info("Вывести список жанров");
        return genresService.findAll();
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable(value = "genreId") Long genreId) {
        log.info("Получить жанр по id= {}", genreId);
        return genresService.getGenreById(genreId);
    }
}
