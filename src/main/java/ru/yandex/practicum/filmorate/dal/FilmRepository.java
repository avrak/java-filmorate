package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmLikeRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
public class FilmRepository extends BaseRepository<Film> {
    private static FilmLikeRepository filmLikeRepository;
    private static GenresRepository genresRepository;

    private static final String FIND_ALL_QUERY = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.code as mpa_code "
            + "FROM films f JOIN mpa m on m.id = f.mpa_id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.code as mpa_code "
            + "FROM films f JOIN mpa m on m.id = f.mpa_id WHERE f.id = ?";
    private static final String FIND_POPULAR_QUERY = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.code as mpa_code "
            + "FROM films f "
            + "JOIN mpa m on m.id = f.mpa_id "
            + "JOIN likes k ON k.film_id = f.id "
            + "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.code "
            + "ORDER BY count(1) DESC "
            + "LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";

    public FilmRepository(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
        filmLikeRepository = new FilmLikeRepository(jdbc, new FilmLikeRowMapper());
        genresRepository = new GenresRepository(jdbc, new GenreRowMapper());
    }

    private Set<FilmLike> getFilmLikes(Long filmId) {
        return new HashSet<>(filmLikeRepository.findByFilmId(filmId));
    }

    private Set<Genre> getFilmGenres(Long filmId) {
        return new HashSet<>(genresRepository.findByFilmId(filmId));
    }

    public List<Film> findAll() {
        List<Film> filmList = findMany(FIND_ALL_QUERY);

        filmList.forEach(film -> {
            film.setLikes(getFilmLikes(film.getId()));
            film.setGenres(getFilmGenres(film.getId()));
        }
        );

        return filmList;
    }

    public Optional<Film> findById(long filmId) {
        Film film = findOne(FIND_BY_ID_QUERY, filmId).get();

        film.setLikes(getFilmLikes(filmId));
        film.setGenres(getFilmGenres(filmId));

        return Optional.of(film);
    }

    private void saveFilmLikes(Film film) {
        if (film.getLikes() == null || film.getLikes().isEmpty()) return;

        filmLikeRepository.deleteByFilmId(film.getId());

        for (FilmLike filmLike : film.getLikes()) {
            filmLikeRepository.save(film.getId(), filmLike.getUserId());
        }
    }

    private void saveFilmGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) return;

        genresRepository.deleteByFilmId(film.getId());

        for (Genre genre : film.getGenres()) {
            genresRepository.saveFilmGenre(film.getId(), genre.getId());
        }
    }

    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);

        saveFilmLikes(film);
        saveFilmGenres(film);

        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        saveFilmLikes(film);
        saveFilmGenres(film);

        return film;
    }

    public void delete(Long filmId) {
        update(DELETE_BY_ID_QUERY, filmId);
    }

    public List<Film> getPopularFilms(Long popularsCount) {
        List<Film> filmList = findMany(FIND_POPULAR_QUERY, popularsCount);

        filmList.forEach(film -> {
                    film.setLikes(getFilmLikes(film.getId()));
                    film.setGenres(getFilmGenres(film.getId()));
                }
        );

        return filmList;

    }
}
