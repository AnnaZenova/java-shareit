package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Arrays;

@Component
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingRequestDto bookingRequestDto) {
        return post("", userId, bookingRequestDto);
    }

    public ResponseEntity<Object> approveBooking(long ownerId, long bookingId, boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, ownerId, null);
    }

    public ResponseEntity<Object> getBooking(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getUserBookings(long userId, String state) {
        if (!Arrays.asList("ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED")
                .contains(state.toUpperCase())) {
            throw new ValidationException("Unknown state: " + state);
        }
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> getOwnerBookings(long ownerId, String state) {
        return get("/owner?state=" + state, ownerId);
    }
}