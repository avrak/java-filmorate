package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Mpa;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.*;

@Validated
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MpaController.class);

    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> findAll() {
        log.trace("Вывести список mpa");
        return mpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa getGenreById(@PathVariable(value = "mpaId") Integer mpaId) {
        return mpaService.getMpaById(mpaId);
    }
}
