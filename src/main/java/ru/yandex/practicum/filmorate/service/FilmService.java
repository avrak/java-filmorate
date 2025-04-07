package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService  {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final FilmLikeRepository filmLikeRepository;
    private final GenresRepository genresRepository;

    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    public Film getFilmById(Long filmId) {
        Optional<Film> film = filmRepository.findById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден.");
        }

        return film.get();
    }

    public Film create(Film film) {
        if (mpaRepository.findById(film.getMpa().getId()).isEmpty()) {
            throw new NotFoundException("Рейтинг с id " + film.getMpa().getId() + " не найден.");
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            StringBuilder genresIdListRow = new StringBuilder();

            for (Genre genre : film.getGenres()) {
                if (!genresIdListRow.isEmpty()) {
                    genresIdListRow.append(",");
                }
                genresIdListRow.append(genre.getId());
            }

            Map<Integer, Genre> existingGenresId = new HashMap<>();
            genresRepository.findByListId(genresIdListRow.toString())
                    .forEach(genre -> existingGenresId.put(genre.getId(), genre));

            for (Genre genre : film.getGenres()) {
                if (existingGenresId.get(genre.getId()) == null) {
                    throw new NotFoundException("Жанр с id " + genre.getId() + " не найден.");
                }
            }
        }

        return filmRepository.save(film);
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ParameterNotValidException("Id должен быть указан");
        }

        Film oldFilm = getFilmById(newFilm.getId());

        if (!newFilm.getName().isEmpty()) {
            oldFilm.setName(newFilm.getName());
        }

        if (!newFilm.getDescription().isEmpty()) {
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getMpa() != null) {
            oldFilm.setMpa(newFilm.getMpa());
        }

        filmRepository.update(oldFilm);
        return oldFilm;
    }

    public void setLikeByUserId(Long filmId, Long userId) {
        if (getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + filmId + " не найден");
        }

        filmLikeRepository.save(filmId, userId);
    }

    public void deleteLikeByUserId(Long filmId, Long userId) {

        if (getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + filmId + " не найден");
        }

        filmLikeRepository.deleteByFilmIdUserId(filmId, userId);
    }

    public Collection<Film> getPopularFilms(Long count) {
        return filmRepository.getPopularFilms(count);
    }
}
