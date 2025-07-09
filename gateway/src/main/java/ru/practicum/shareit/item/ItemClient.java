package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.comment.CommentRequestDto;

import java.util.List;

@Component
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemRequestDto itemRequestDto) {
        return patch("/" + itemId, userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchAvailableItems(String text) {
        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        return get("/search?text=" + text, 0);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentRequestDto commentRequestDto) {
        return post("/" + itemId + "/comment", userId, commentRequestDto);
    }
}