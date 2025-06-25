package ru.practicum.shareit.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private long id;
    @NotNull
    private String description;
    @NotNull
    private User requester;
    private LocalDateTime created;
}