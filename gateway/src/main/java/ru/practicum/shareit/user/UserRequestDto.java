package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;

    @NotBlank(message = "Name cannot be blank", groups = ValidationGroups.Create.class)
    private String name;

    @NotBlank(message = "Email cannot be blank", groups = ValidationGroups.Create.class)
    @Email(message = "Email должен быть валидным", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String email;
}