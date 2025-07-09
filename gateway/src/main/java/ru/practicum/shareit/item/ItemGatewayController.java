package ru.practicum.shareit.item;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.comment.CommentRequestDto;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemGatewayController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItem(@RequestHeader(BaseClient.USER_ID_HEADER) long userId,
                                          @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Gateway: добавление вещи пользователем {}", userId);
        return itemClient.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@RequestHeader(BaseClient.USER_ID_HEADER) long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Gateway: обновление вещи {} пользователем {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader(BaseClient.USER_ID_HEADER) Long userId) {
        log.info("Gateway: получение вещи {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserItems(@RequestHeader(BaseClient.USER_ID_HEADER) long userId) {
        log.info("Gateway: получение всех вещей пользователя {}", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchAvailableItems(@RequestParam String text) {
        log.info("Gateway: поиск вещей по запросу '{}'", text);

        return itemClient.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@RequestHeader(BaseClient.USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Gateway: добавление комментария к вещи {}", itemId);
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }
}