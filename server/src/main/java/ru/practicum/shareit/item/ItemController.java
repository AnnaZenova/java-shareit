package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.booking.BookingController;


import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(BookingController.USER_ID_HEADER) long userId,
                           @RequestBody ItemDto itemDto) {
        log.info("Вещь успешно добавлена с ID: {}", itemDto.getId());
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader(BookingController.USER_ID_HEADER) long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable long itemId) {
        log.info("Вещь с ID: {} успешно обновлена", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ItemDto> getItem(@PathVariable Long itemId,
                                           @RequestHeader(BookingController.USER_ID_HEADER) Long userId) {
        log.info("Возвращена вещь с ID: {}", itemId);
        return  ResponseEntity.ok(itemService.getItemById(itemId, userId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> getUserItems(@RequestHeader(BookingController.USER_ID_HEADER) long userId) {
        log.info("Найдены вещи пользователя с ID: {}", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDto> searchAvailableItems(@RequestParam String text) {
        log.info("Найдены вещей по запросу '{}'", text);
        return itemService.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDto> addComment(@RequestHeader(BookingController.USER_ID_HEADER) Long userId,
                                                 @PathVariable Long itemId,
                                                 @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(itemService.addComment(userId, itemId, commentDto));
    }
}