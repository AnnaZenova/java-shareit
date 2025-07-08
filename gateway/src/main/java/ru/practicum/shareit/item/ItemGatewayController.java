package ru.practicum.shareit.item;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentRequestDto;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemGatewayController {
    private final ItemClient itemClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(USER_ID_HEADER) long userId,
                                          @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Gateway: добавление вещи пользователем {}", userId);
        return itemClient.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Gateway: обновление вещи {} пользователем {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Gateway: получение вещи {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Gateway: получение всех вещей пользователя {}", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchAvailableItems(@RequestParam String text) {
        log.info("Gateway: поиск вещей по запросу '{}'", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        return itemClient.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Gateway: добавление комментария к вещи {}", itemId);
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }
}