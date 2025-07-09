package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.BaseClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingGatewayController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId,
                                                @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Gateway: запрос на создание бронирования от пользователя ID: {}", userId);
        return bookingClient.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> approveBooking(@RequestHeader(BaseClient.USER_ID_HEADER) Long ownerId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        log.info("Gateway: запрос на подтверждение бронирования ID: {} от владельца ID: {}", bookingId, ownerId);
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBooking(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Gateway: запрос на получение бронирования ID: {} от пользователя ID: {}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserBookings(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        log.info("Gateway: запрос на получение бронирований пользователя ID: {}", userId);
        return bookingClient.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(BaseClient.USER_ID_HEADER) Long ownerId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.info("Gateway: запрос на получение бронирований владельца ID: {}", ownerId);
        return bookingClient.getOwnerBookings(ownerId, state);
    }
}