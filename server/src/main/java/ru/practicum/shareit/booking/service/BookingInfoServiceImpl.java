package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingInfoServiceImpl implements BookingInfoService {
    private final BookingRepository bookingRepository;

    @Override
    public BookingShortDto getLastBookingForItem(Long itemId) {
        log.info("Результат поиска последнего бронирования для вещи ID: {}", itemId);
        return bookingRepository.findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
                        itemId,
                        LocalDateTime.now(),
                        Status.APPROVED)
                .map(booking -> new BookingShortDto(booking.getId(), booking.getBooker().getId()))
                .orElse(null);
    }

    @Override
    public BookingShortDto getNextBookingForItem(Long itemId) {
        log.info("Поиск следующего бронирования для вещи с ID: {}", itemId);
        return bookingRepository.findFirstByItemIdAndStartAfterAndStatusIn(
                        itemId,
                        LocalDateTime.now(),
                        List.of(Status.APPROVED))
                .map(booking -> new BookingShortDto(booking.getId(), booking.getBooker().getId()))
                .orElse(null);
    }

    @Override
    public boolean hasUserBookedItem(Long userId, Long itemId) {
        log.debug("Проверка, бронировал ли пользователь ID: {} вещь ID: {}", userId, itemId);
        return bookingRepository.existsByBookerIdAndItemIdAndEndBefore(
                userId,
                itemId,
                LocalDateTime.now());
    }
}
