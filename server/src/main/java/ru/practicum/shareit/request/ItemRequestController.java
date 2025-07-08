package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @Transactional
    public ResponseEntity<ItemRequestDto> createRequest(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequestDto createdRequest = itemRequestService.createRequest(userId, itemRequestDto);
        log.info("Server:Создан новый запрос с ID: {}", createdRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestWithItemsDto>> getUserRequests(
            @RequestHeader(USER_ID_HEADER) Long userId) {
        List<ItemRequestWithItemsDto> requests = itemRequestService.getUserRequests(userId);
        log.info("Server:Получено {} запросов пользователя с ID: {}", requests.size(), userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestWithItemsDto>> getAllRequests(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        List<ItemRequestWithItemsDto> requests = itemRequestService.getAllRequests(userId, from, size);
        log.info("Server:Получено {} запросов других пользователей", requests.size());
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestWithItemsDto> getRequestById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long requestId) {
        ItemRequestWithItemsDto request = itemRequestService.getRequestById(userId, requestId);
        log.info("Server: Получен запрос с ID: {}", requestId);
        return ResponseEntity.ok(request);
    }
}