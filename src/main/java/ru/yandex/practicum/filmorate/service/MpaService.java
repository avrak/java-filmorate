package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository mpaRepository;

    public Collection<Mpa> findAll() {
        return mpaRepository.findAll();
    }

    public Mpa getMpaById(Integer ratingId) {
        Optional<Mpa> rating = mpaRepository.findById(ratingId);

        if (rating.isEmpty()) throw new NotFoundException("Рейтинг с id " + ratingId + " не найден");

        return rating.get();
    }
}
