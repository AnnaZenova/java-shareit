package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class Booking {

    @NotNull(message = "ID не может быть пустой")
    private long id;

    @NotNull(message = "Start date не может быть пустой")
    @FutureOrPresent(message = "Start date должна быть в настоящем или будущем")
    private LocalDateTime start;

    @NotNull(message = "End date не можеть быть пустой")
    @Future(message = "End date должна быть в будущем")
    private LocalDateTime end;

    @NotNull(message = "Item не может быть пустым")
    private Item item;

    @NotNull(message = "Booker не может быть пустым")
    private User booker;
    private Status status;
}

