package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    @NotNull(message = "ID не может быть пустой")
    private Long id;

    @NotNull(message = "Booker ID не может быть пустой")
    private Long booker;

    @NotNull(message = "Start date не может быть пустой")
    @FutureOrPresent(message = "Start date должна быть в настоящем или будущем")
    private LocalDateTime start;

    @NotNull(message = "End date не можеть быть пустой")
    @Future(message = "End date должна быть в будущем")
    private LocalDateTime end;

    @NotNull(message = "Item ID не может быть пустой")
    private Long item;
    private Status status;

    public BookingDto(LocalDateTime start, LocalDateTime end, long item, long booker, Status status) {
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}