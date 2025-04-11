package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {

    private static final String FIND_ALL_QUERY = "SELECT id, email, login, name, birthday FROM users u";
    private static final String FIND_BY_ID_QUERY = "SELECT id, email, login, name, birthday FROM users u WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    private static final String GET_FRIENDS_BY_USER_ID = "SELECT u.id, u.email, u.login, u.name, u.birthday "
            + "FROM friends f JOIN users u ON u.id = f.friendId "
            + "WHERE userId = ? ";

    private static final String GET_COMMON_FRIENDS_BY_USER_ID = "SELECT u.id, u.email, u.login, u.name, u.birthday "
                    + "FROM friends f JOIN users u ON u.id = f.friendId "
                    + "WHERE f.userId = ? AND f.friendId != ?"
            + "UNION "
                    + "SELECT u.id, u.email, u.login, u.name, u.birthday "
                    + "FROM friends ff JOIN users u ON u.id = ff.friendId "
                    + "WHERE ff.userId = ? AND ff.friendId != ?";

    private static final String INSERT_FRIENDS_QUERY = "INSERT INTO friends(userId, friendId) VALUES(?, ?)";
    private static final String DELETE_FRIENDS_BY_ID_QUERY = "DELETE FROM friends WHERE userId = ? AND friendId = ?";

    public UserRepository(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public User save(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public List<User> findFriendsByUserId(long userId) {
        return findMany(GET_FRIENDS_BY_USER_ID, userId);
    }

    public List<User> findCommonFriendsByUserId(long userId1, long userId2) {
        return findMany(GET_COMMON_FRIENDS_BY_USER_ID, userId1, userId2, userId1, userId2);
    }


    public void saveFriends(long userId1, long userId2) {
        insertNoKey(
                INSERT_FRIENDS_QUERY,
                userId1,
                userId2
        );
    }

    public void deleteFriends(long userId, long friendId) {
        jdbc.update(DELETE_FRIENDS_BY_ID_QUERY,
                userId,
                friendId
        );
    }

}