package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingController;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ItemRequestDto createRequest(@RequestHeader(BookingController.USER_ID_HEADER) Long userId,
                                        @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequestDto createdRequest = itemRequestService.createRequest(userId, itemRequestDto);
        log.info("Server:Создан новый запрос с ID: {}", createdRequest.getId());
        return createdRequest;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getUserRequests(@RequestHeader(BookingController.USER_ID_HEADER) Long userId) {
        List<ItemRequestWithItemsDto> requests = itemRequestService.getUserRequests(userId);
        log.info("Server:Получено {} запросов пользователя с ID: {}", requests.size(), userId);
        return requests;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getAllRequests(@RequestHeader(BookingController.USER_ID_HEADER) Long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        List<ItemRequestWithItemsDto> requests = itemRequestService.getAllRequests(userId, from, size);
        log.info("Server:Получено {} запросов других пользователей", requests.size());
        return requests;
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestWithItemsDto getRequestById(@RequestHeader(BookingController.USER_ID_HEADER) Long userId,
                                                  @PathVariable Long requestId) {
        log.info("Server: Получен запрос с ID: {}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }
}