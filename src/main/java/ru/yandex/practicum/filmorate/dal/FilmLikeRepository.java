package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmLikeRowMapper;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

@Repository
public class FilmLikeRepository extends BaseRepository<FilmLike> {
    private static final String GET_LIKES_BY_FILM_ID = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO likes(film_id, user_id) VALUES(?, ?)";
    private static final String DELETE_BY_FILMID_USERID_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM likes WHERE film_id = ?";

    public FilmLikeRepository(JdbcTemplate jdbc, FilmLikeRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<FilmLike> findByFilmId(long filmId) {
        return findMany(GET_LIKES_BY_FILM_ID, filmId);
    }

    public void save(long filmId, long userId) {
        insertNoKey(
                INSERT_QUERY,
                filmId,
                userId
        );
    }

    public void deleteByFilmIdUserId(long filmId, long userId) {
        jdbc.update(DELETE_BY_FILMID_USERID_QUERY,
                filmId,
                userId
        );
    }

    public void deleteByFilmId(long filmId) {
        jdbc.update(DELETE_QUERY, filmId);
    }
}
