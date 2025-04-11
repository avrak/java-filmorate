package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class NewFriendsRequest {
    private Long user1;
    private Long user2;
}
