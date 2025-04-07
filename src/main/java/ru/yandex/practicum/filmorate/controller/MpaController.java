package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Mpa;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.*;

@Validated
@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> findAll() {
        log.info("Вывести список mpa");
        return mpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa getGenreById(@PathVariable(value = "mpaId") Integer mpaId) {
        log.info("Вывести mpa с id = {}", mpaId);
        return mpaService.getMpaById(mpaId);
    }
}
