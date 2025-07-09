package ru.practicum.shareit.item.request;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.BaseClient;


@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestGatewayController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId,
                                                @Valid @RequestBody ItemRequestGatewayDto itemRequestGatewayDto) {
        log.info("Gateway: создание запроса пользователем ID: {}", userId);
        return itemRequestClient.createRequest(userId, itemRequestGatewayDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserRequests(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId) {
        log.info("Gateway: получение запросов пользователя ID: {}", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllRequests(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId) {
        log.info("Gateway: получение всех запросов пользователя с ID {}", userId);
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestById(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Gateway: получение запроса ID: {}", requestId);
        return itemRequestClient.getRequestById(userId, requestId);
    }
}