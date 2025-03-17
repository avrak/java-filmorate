package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FirstFilmDateIdValidator implements ConstraintValidator<NotEarliest, LocalDate> {
    public static final int EARLIEST_FILM_YEAR = 1895;
    public static final int EARLIEST_FILM_MONTH = 12;
    public static final int EARLIEST_FILM_DAY = 28;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {

        if (date != null) {
            return date.isAfter(LocalDate.of(EARLIEST_FILM_YEAR, EARLIEST_FILM_MONTH, EARLIEST_FILM_DAY));
        }
        return true;
    }
}
