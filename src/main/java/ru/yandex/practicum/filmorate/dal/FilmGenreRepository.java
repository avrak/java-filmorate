package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

@Repository
public class FilmGenreRepository extends BaseRepository<FilmGenre> {
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT film_id, genre_id FROM film_genres WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmGenre> findByFilmId(long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    public void save(FilmGenre filmGenre) {
        insertNoKey(
                INSERT_QUERY,
                filmGenre.getFilmId(),
                filmGenre.getGenreId()
        );
    }

    public void deleteByFilmId(Long filmId) {
        jdbc.update(DELETE_QUERY, filmId);
    }
}
