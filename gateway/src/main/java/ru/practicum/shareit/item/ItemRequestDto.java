package ru.practicum.shareit.item;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Name не может быть пустой")
    private String name;

    @NotBlank(message = "Description не может быть пустым")
    private String description;

    @NotNull(message = "Available не может быть пустым")
    private Boolean available;

    private Long requestId;
}