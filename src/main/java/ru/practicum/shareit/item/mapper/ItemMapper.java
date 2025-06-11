package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId()
        );
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        itemDto.getOwner();
        Item item = new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
        item.setId(itemDto.getId()); // Устанавливаем ID, если есть
        item.setOwner(owner);        // Явно передаём владельца
        return item;
    }
}