package ru.practicum.shareit.item.request;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestGatewayController {
    private final ItemRequestClient itemRequestClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody ItemRequestGatewayDto itemRequestGatewayDto) {
        log.info("Gateway: создание запроса пользователем ID: {}", userId);
        return itemRequestClient.createRequest(userId, itemRequestGatewayDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(
            @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Gateway: получение запросов пользователя ID: {}", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(
            @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Gateway: получение всех запросов пользователя с ID {}", userId);
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long requestId) {
        log.info("Gateway: получение запроса ID: {}", requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }
}