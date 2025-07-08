package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingGatewayController {
    private final BookingClient bookingClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                                @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Gateway: запрос на создание бронирования от пользователя ID: {}", userId);
        return bookingClient.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        log.info("Gateway: запрос на подтверждение бронирования ID: {} от владельца ID: {}", bookingId, ownerId);
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Gateway: запрос на получение бронирования ID: {} от пользователя ID: {}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(USER_ID_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        if (!Arrays.asList("ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED")
                .contains(state.toUpperCase())) {
            throw new ValidationException("Unknown state: " + state);
        }
        log.info("Gateway: запрос на получение бронирований пользователя ID: {}", userId);
        return bookingClient.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.info("Gateway: запрос на получение бронирований владельца ID: {}", ownerId);
        return bookingClient.getOwnerBookings(ownerId, state);
    }
}