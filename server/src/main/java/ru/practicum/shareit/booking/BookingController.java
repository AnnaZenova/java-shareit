package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на создание бронирования от пользователя ID: {}", userId);
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                     @PathVariable Long bookingId,
                                     @RequestParam boolean approved) {
        log.info("Получен запрос на подтверждение бронирования ID: {} от владельца ID: {}", bookingId, ownerId);
        log.debug("Статус подтверждения: {}", approved ? "APPROVED" : "REJECTED");
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @PathVariable Long bookingId) {
        log.info("Получен запрос на получение бронирования ID: {} от пользователя ID: {}", bookingId, userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getUserBookings(@RequestHeader(USER_ID_HEADER) Long userId,
                                            @RequestParam(defaultValue = "ALL") String state) {
        bookingService.validateBookingState(state);
        log.info("Получен запрос на получение бронирований пользователя ID: {}", userId);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getOwnerBookings(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос на получение бронирований пользователя ID: {}", ownerId);
        return bookingService.getOwnerBookings(ownerId, state);
    }
}