package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Long requestId;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments = List.of();

}