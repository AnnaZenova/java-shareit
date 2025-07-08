package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingShortDto {
    private Long id;
    private Long bookerId;
}