package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank(message = "Name не может быть пустой")
    private String name;

    @NotBlank(message = "Description не может быть пустой")
    private String description;

    @NotNull(message = "Available не может быть пустой")
    private Boolean available;
    @NotNull(message = "Owner не может быть пустой")
    private User owner;
    private ItemRequest request;

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}