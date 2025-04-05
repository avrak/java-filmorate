package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenresRepository extends BaseRepository<Genre> {

    private static final String FIND_ALL_QUERY = "SELECT id, name FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM genres WHERE id = ?";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT DISTINCT id, name "
        + "FROM film_genres FG JOIN genres g ON g.id = fg.genre_id WHERE fg.film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_BY_FILMD_ID_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    public GenresRepository(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public List<Genre> findByFilmId(long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    public void saveFilmGenre(long filmId, long genreId) {
        insertNoKey(
                INSERT_QUERY,
                filmId,
                genreId
        );
    }

    public void deleteByFilmId(long filmId) {
        jdbc.update(DELETE_BY_FILMD_ID_QUERY, filmId);
    }

}
