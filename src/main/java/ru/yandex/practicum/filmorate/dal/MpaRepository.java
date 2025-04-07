package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String GET_ID_QUERY = "SELECT id, code FROM mpa WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, code FROM mpa";

    public MpaRepository(JdbcTemplate jdbc, MpaRowMapper mapper) {
        super(jdbc, mapper);
    }

    public Optional<Mpa> findById(int id) {
        return findOne(GET_ID_QUERY, id);
    }

    public Collection<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
