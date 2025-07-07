package ru.practicum.shareit.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Name не может быть пустой")
    private String name;

    @NotBlank(message = "Email не может быть пустой")
    @Email(message = "Email должен быть валидным")
    @Column
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}