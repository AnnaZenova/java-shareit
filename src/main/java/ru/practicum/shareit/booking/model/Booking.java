package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID не может быть пустой")
    private long id;

    @NotNull(message = "Start date не может быть пустой")
    @FutureOrPresent(message = "Start date должна быть в настоящем или будущем")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @NotNull(message = "End date не можеть быть пустой")
    @Future(message = "End date должна быть в будущем")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @NotNull(message = "Item не может быть пустым")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NotNull(message = "Booker не может быть пустым")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}

