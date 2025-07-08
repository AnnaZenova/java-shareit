package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                                    @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на создание бронирования от пользователя ID: {}", userId);
        return ResponseEntity.ok(bookingService.createBooking(userId, bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                     @PathVariable Long bookingId,
                                                     @RequestParam boolean approved) {
        log.info("Получен запрос на подтверждение бронирования ID: {} от владельца ID: {}", bookingId, ownerId);
        log.debug("Статус подтверждения: {}", approved ? "APPROVED" : "REJECTED");
        return ResponseEntity.ok(bookingService.approveBooking(ownerId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Получен запрос на получение бронирования ID: {} от пользователя ID: {}", bookingId, userId);
        return ResponseEntity.ok(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(@RequestHeader(USER_ID_HEADER) Long userId,
                                                            @RequestParam(defaultValue = "ALL") String state) {
        if (!Arrays.asList("ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED")
                .contains(state.toUpperCase())) {
            throw new ValidationException("Неизвестный state: " + state);
        }
        log.info("Получен запрос на получение бронирований пользователя ID: {}", userId);
        return ResponseEntity.ok(bookingService.getUserBookings(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                             @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос на получение бронирований пользователя ID: {}", ownerId);
        return ResponseEntity.ok(bookingService.getOwnerBookings(ownerId, state));
    }
}