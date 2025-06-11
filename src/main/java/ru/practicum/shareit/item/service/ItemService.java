package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    Collection<ItemDto> getUserItems(Long userId);

    ItemDto addItem(Long userId, ItemDto itemDto);

    Collection<ItemDto> searchAvailableItems(String text);

    Item getItemEntity(Long itemId);
}