package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;
    private long currentMaxId = 0L;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
    }

    @Override
    public Film getFilmById(Long filmId) {
        return films.get(filmId);
    }

    @Override
    public Map<Long, Film> getAll() {
        return films;
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(Long filmId) {
        films.remove(filmId);
    }

    @Override
    public long getNextId() {
        return ++currentMaxId;
    }

    @Override
    public void setLikeByUserId(Long filmId, Long userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLikeByUserId(Long filmId, Long userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public Collection<Film> getPopularFilms(Long count) {
        return new ArrayList<>(films.values())
                .stream()
                .sorted((Film f1, Film f2) -> -1 * (f1.getLikes().size() - f2.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
