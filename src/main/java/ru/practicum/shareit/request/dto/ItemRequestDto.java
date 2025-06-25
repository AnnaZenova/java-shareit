package ru.practicum.shareit.request.dto;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private long id;

    @NotBlank(message = "Description не может быть пустым")
    private String description;

    @NotNull(message = "Requester ID не может быть пустым")
    private long requester;
    private LocalDateTime created;

    public ItemRequestDto(String description, long requester, LocalDateTime created) {
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}