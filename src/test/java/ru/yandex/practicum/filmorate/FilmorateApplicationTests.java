package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@SpringBootTest
class FilmorateApplicationTests {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	public void checkUser() {
		User user = new User();

		user.setEmail("xxx");
		user.setLogin("");
		user.setBirthday(LocalDate.of(2222,1,1));

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		ArrayList<String> desc = new ArrayList<>();

		violations.forEach(v -> desc.add(v.getMessage()));

		assertFalse(violations.isEmpty(), desc.toString());
	}

	@Test
	void checkFilm() {
		Film film = new Film();

		film.setName("");

		StringBuilder description = new StringBuilder();
		for (int i = 0; i < 205; i++) description.append("x");

		film.setDescription(description.toString());
		film.setReleaseDate(LocalDate.of(1000, 1, 1));

		Set<ConstraintViolation<Film>> violations = validator.validate(film);

		ArrayList<String> desc = new ArrayList<>();

		violations.forEach(v -> desc.add(v.getMessage()));

		assertFalse(violations.isEmpty(), desc.toString());
	}
}