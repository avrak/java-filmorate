package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface FilmStorage {
    public Film getFilmById(Long filmId);

    public Map<Long, Film> getAll();

    public Film create(Film film);

    public Film update(Film newFilm);

    public void delete(Long filmId);

    public long getNextId();

    public void setLikeByUserId(Long filmId, Long userId);

    public void deleteLikeByUserId(Long filmId, Long userId);

    public Collection<Film> getPopularFilms(Long count);
}
