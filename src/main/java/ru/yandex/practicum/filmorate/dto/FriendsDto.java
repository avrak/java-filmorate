package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FriendsDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long user1;
    private Long user2;
}
