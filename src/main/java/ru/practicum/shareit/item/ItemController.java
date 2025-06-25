package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID_HEADER) @NotNull long userId,
                           @RequestBody @Valid ItemDto itemDto) {
        log.info("Вещь успешно добавлена с ID: {}", itemDto.getId());
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID_HEADER) @NotNull long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable long itemId) {
        log.info("Вещь с ID: {} успешно обновлена", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        log.info("Возвращена вещь с ID: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getUserItems(@RequestHeader(USER_ID_HEADER) @NotNull long userId) {
        log.info("Найдены вещи пользователя с ID: {}", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchAvailableItems(@RequestParam String text) {
        if (text.isBlank()) {
            return List.of();
        }
        log.info("Найдены вещей по запросу '{}'", text);
        return itemService.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(itemService.addComment(userId, itemId, commentDto));
    }
}