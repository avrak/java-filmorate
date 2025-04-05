package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GenresService {

    private final GenresRepository genresRepository;

    public Collection<Genre> findAll() {
        return genresRepository.findAll();
    }

    public Genre getGenreById(Long genreId) {
        Optional<Genre> genre = genresRepository.findById(genreId);

        if (genre.isEmpty()) throw new NotFoundException("Жанр с id " + genreId + " не найден");

        return genre.get();
    }
}
