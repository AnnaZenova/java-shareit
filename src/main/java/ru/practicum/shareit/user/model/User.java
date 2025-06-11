package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Long id;

    @NotBlank(message = "Name не может быть пустой")
    private String name;

    @NotBlank(message = "Email не может быть пустой")
    @Email(message = "Email должен быть валидным")
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}