package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class NewMpaRequest {
    private Integer ratingId;
    private String code;
}
