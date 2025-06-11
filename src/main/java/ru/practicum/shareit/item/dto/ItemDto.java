package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

    private long id;

    @NotBlank(message = "Name не может быть пустой")
    private String name;

    @NotBlank(message = "Description не может быть пустым")
    private String description;

    @NotNull(message = "Available не может быть пустым")
    private Boolean available;

    private Long owner;
    private long request;

    public ItemDto(long id, String name, String description, Boolean available, Long owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}