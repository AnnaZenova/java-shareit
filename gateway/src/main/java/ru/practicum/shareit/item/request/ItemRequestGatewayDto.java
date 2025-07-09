package ru.practicum.shareit.item.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestGatewayDto {
    @NotBlank(message = "Description не может быть пустым")
    private String description;
    private long requester;
    private LocalDateTime created;
}