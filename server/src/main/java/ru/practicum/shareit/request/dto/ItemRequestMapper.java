package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequester(itemRequest.getRequester().getId());
        dto.setCreated(itemRequest.getCreated());
        return dto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requester) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreated(itemRequestDto.getCreated() != null ?
                itemRequestDto.getCreated() : LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestWithItemsDto toItemRequestWithItemsDto(ItemRequest itemRequest, List<Item> items) {
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());

        List<ItemForRequestDto> itemsDto = items.stream()
                .map(item -> new ItemForRequestDto(
                        item.getId(),
                        item.getName(),
                        item.getOwner().getId()))
                .collect(Collectors.toList());

        dto.setItems(itemsDto);
        return dto;
    }
}

