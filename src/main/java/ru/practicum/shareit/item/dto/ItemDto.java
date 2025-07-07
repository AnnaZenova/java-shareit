package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

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

    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;

    @Builder.Default
    private List<CommentDto> comments = List.of();

}