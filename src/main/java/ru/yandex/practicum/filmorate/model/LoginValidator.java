package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<NoSpaces, String>{
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return !login.contains(" ");
    }

}
