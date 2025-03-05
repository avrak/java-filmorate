package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
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
		userController = new UserController();

		user = new User();
		user.setLogin("avrak");
		user.setName("Alex");
		user.setEmail("avrak@yandex.ru");
		user.setBirthday(LocalDate.of(1972,2,10));

		filmController = new FilmController();

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
		ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));

		assertEquals("Логин пользователя не может быть пустым", thrown.getMessage());
	}

	@Test
	public void checkWrongLogin() {
		user.setLogin("av rak");
		ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));

		assertEquals("Логин пользователя не должен содержать пробелы", thrown.getMessage());
	}

	@Test
	public void checkEmptyEmail() {
		user.setEmail("");
		ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));

		assertEquals("Имейл должен быть указан", thrown.getMessage());
	}

	@Test
	public void checkWrongEmail() {
		user.setEmail("avrak");
		ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));

		assertEquals("Некорректный имейл", thrown.getMessage());
	}

	@Test
	public void checkDuplicatedEmail() {
		userController.create(user);

		User newUser = new User();
		newUser.setLogin("vgrak");
		newUser.setName("Vlad");
		newUser.setEmail("avrak@yandex.ru");
		newUser.setBirthday(LocalDate.of(1924,6,29));

		ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(newUser));

		assertEquals("Этот имейл уже используется", thrown.getMessage());
	}

	@Test
	public void checkWrongBirthday() {
		user.setBirthday(LocalDate.of(2972,2,10));
		ValidationException thrown = assertThrows(ValidationException.class, () -> userController.create(user));

		assertEquals("День рождения пользователя не может быть в будущем", thrown.getMessage());
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

		ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.create(film));

		assertEquals("Название фильма не может быть пустым", thrown.getMessage());
	}

	@Test
	public void checkWrongDescription() {
		while (film.getDescription().length() <= 200) {
			film.setDescription(film.getDescription() + ".");
		}

		ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.create(film));

		assertEquals("Описание должно быть не более 200 символов", thrown.getMessage());
	}

	@Test
	public void checkWrongDuration() {
		film.setDuration(-1);

		ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.create(film));

		assertEquals("Продолжительность фильма должна быть положительным числом", thrown.getMessage());
	}

	@Test
	public void checkReleaseDate() {
		film.setReleaseDate(LocalDate.of(1794, 5, 21));

		ValidationException thrown = assertThrows(ValidationException.class, () -> filmController.create(film));

		assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", thrown.getMessage());
	}
}
