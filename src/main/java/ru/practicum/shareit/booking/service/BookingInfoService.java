package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingShortDto;

public interface BookingInfoService {
    BookingShortDto getLastBookingForItem(Long itemId);

    BookingShortDto getNextBookingForItem(Long itemId);

    boolean hasUserBookedItem(Long userId, Long itemId);
}