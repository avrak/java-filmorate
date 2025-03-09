package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;


@Component
public interface FilmStorage {
    public Film getFilmById(Long filmId);

    public Film create(Film film);

    public Film update(Film newFilm);

    public void validateFilm(Film film);

    public void validateFilmName(Film film);

    public void validateFilmDescription(Film film);

    public void validateFilmDuration(Film film);

    public void validateFilmReleaseDate(Film film);

    public long getNextId();

    public void setLikeByUserId(Long filmId, Long userId);

    public void deleteLikeByUserId(Long filmId, Long userId);

    public Collection<Film> getPopularFilms(Long count);
}
