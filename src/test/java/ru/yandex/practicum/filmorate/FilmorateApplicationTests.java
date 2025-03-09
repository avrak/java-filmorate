package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {
	UserController userController;
	FilmController filmController;
	User user;
	Film film;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void setEntities() {
		InMemoryStorage inMemoryStorage = new InMemoryStorage(new InMemoryUserStorage(), new InMemoryFilmStorage());

		userController = new UserController(new UserService(inMemoryStorage));

		user = new User();
		user.setLogin("avrak");
		user.setName("Alex");
		user.setEmail("avrak@yandex.ru");
		user.setBirthday(LocalDate.of(1972,2,10));

		filmController = new FilmController(new FilmService(inMemoryStorage));

		film = new Film();
		film.setName("Pulp Fiction");
		film.setReleaseDate(LocalDate.of(1994, 5, 21));
		film.setDescription("Культовый кинофильм режиссёра Квентина Тарантино");
		film.setDuration(154);
	}

	@Test
	public void addNewUserCheck() {
		userController.create(user);

        assertFalse(userController.findAll().stream().noneMatch(user -> user.getLogin().equals("avrak")), "Пользователь не был добавлен");
	}

	@Test
	public void updateUserCheck() {
		User newUser = userController.create(user);

		newUser.setName("Alexander");

		userController.update(newUser);

		assertFalse(userController.findAll().stream().noneMatch(user -> user.getName().equals("Alexander")), "Пользователь не был обновлен");
	}

	@Test
	public void checkEmptyLogin() {
		user.setLogin("");
		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> userController.create(user));

		assertEquals("Логин пользователя не может быть пустым", thrown.getReason());
	}

	@Test
	public void checkWrongLogin() {
		user.setLogin("av rak");
		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> userController.create(user));

		assertEquals("Логин пользователя не должен содержать пробелы", thrown.getReason());
	}

	@Test
	public void checkEmptyEmail() {
		user.setEmail("");
		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> userController.create(user));

		assertEquals("Имейл должен быть указан", thrown.getReason());
	}

	@Test
	public void checkWrongEmail() {
		user.setEmail("avrak");
		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> userController.create(user));

		assertEquals("Некорректный имейл", thrown.getReason());
	}

	@Test
	public void checkDuplicatedEmail() {
		userController.create(user);

		User newUser = new User();
		newUser.setLogin("vgrak");
		newUser.setName("Vlad");
		newUser.setEmail("avrak@yandex.ru");
		newUser.setBirthday(LocalDate.of(1924,6,29));

		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> userController.create(newUser));

		assertEquals("Этот имейл уже используется", thrown.getReason());
	}

	@Test
	public void checkWrongBirthday() {
		user.setBirthday(LocalDate.of(2972,2,10));
		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> userController.create(user));

		assertEquals("День рождения пользователя не может быть в будущем", thrown.getReason());
	}

	@Test
	public void checkAddFilm() {
		filmController.create(film);

		assertFalse(filmController.findAll().stream().noneMatch(film -> film.getName().equals("Pulp Fiction")), "Фильм не был добавлен");
	}

	@Test
	public void checkFilmUpdate() {
		Film newFilm = filmController.create(film);
		newFilm.setDescription("Сюжет фильма построен нелинейно, как и в большинстве других работ Тарантино");

		assertFalse(filmController.findAll().stream().noneMatch(film -> film.getDescription().equals("Сюжет фильма построен нелинейно, как и в большинстве других работ Тарантино")),"Фильм не был обновлен");
	}

	@Test
	public void checkEmptyName() {
		film.setName("");

		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> filmController.create(film));

		assertEquals("Название фильма не может быть пустым", thrown.getReason());
	}

	@Test
	public void checkWrongDescription() {
		while (film.getDescription().length() <= 200) {
			film.setDescription(film.getDescription() + ".");
		}

		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> filmController.create(film));

		assertEquals("Описание должно быть не более 200 символов", thrown.getReason());
	}

	@Test
	public void checkWrongDuration() {
		film.setDuration(-1);

		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> filmController.create(film));

		assertEquals("Продолжительность фильма должна быть положительным числом", thrown.getReason());
	}

	@Test
	public void checkReleaseDate() {
		film.setReleaseDate(LocalDate.of(1794, 5, 21));

		ParameterNotValidException thrown = assertThrows(ParameterNotValidException.class, () -> filmController.create(film));

		assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", thrown.getReason());
	}

	@Test
	public void checkAddUserFriend() {

		userController.create(user);

		User friend = new User();
		friend.setLogin("vgrak");
		friend.setName("Vlad");
		friend.setEmail("vgrak@yandex.ru");
		friend.setBirthday(LocalDate.of(1924,7,29));

		userController.create(friend);

		userController.addFriend(user.getId(), friend.getId());

		assertTrue(userController
				.getUserById(user.getId())
				.getFriends()
				.contains(userController.getUserById(friend.getId()).getId())
				&& userController
						.getUserById(userController.getUserById(friend.getId()).getId())
						.getFriends()
						.contains(user.getId()),
				"Друзья не найдены");
	}

	@Test
	public void checkLikes() {
		filmController.create(film);
		userController.create(user);
		filmController.getFilmById(film.getId()).getLikes().add(user.getId());

		assertTrue(filmController.getFilmById(film.getId()).getLikes().contains(user.getId()), "Лайк не найден");
	}
}
