package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ElementNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(Long userId,@Valid ItemDto itemDto) {
        checkUserById(userId);
        User owner = userRepository.getUserById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(itemDto,owner);
        item.setOwner(owner);
        log.info("Добавлена вещь :{} пользователю с ID :{}", itemDto, userId);
        return ItemMapper.toItemDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId,@Valid ItemDto itemDto) {
        checkUserById(userId);
        User owner = userRepository.getUserById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(getItemById(itemId),owner);
        if (item.getOwner().getId() != userId) {
            throw new ElementNotFoundException(String.format("вещь с ID :{} у пользователя с ID :{}", item.getId(), userId));
        }
        if (itemDto.getName() != null) {
            if (!itemDto.getName().isBlank()) {
                item.setName(itemDto.getName());
            }
        }
        if (itemDto.getDescription() != null) {
            if (!itemDto.getDescription().isBlank()) {
                item.setDescription(itemDto.getDescription());
            }
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        log.info("Обновлена вещь с ID :{} пользователю с ID :{}", itemId, userId);
        return ItemMapper.toItemDto(itemRepository.updateItem(item));
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.getItemById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format("вещь с ID :{}", id)));
        log.info("Запрошена вещь с id:{}", id);
        return ItemMapper.toItemDto(item);
    }

    private void checkUserById(long userId) {
        if (!userRepository.checkUserById(userId)) {
            throw new ElementNotFoundException(String.format("пользователь с ID :{} не найден", userId));
        }
    }

    @Override
    public Collection<ItemDto> getUserItems(Long userId) {
        checkUserById(userId);
        log.info("Получены вещи пользователя с ID: {}",userId);
        return itemRepository.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchAvailableItems(String text) {
        log.info("Поиск вещей по запросу - {}", text);
        return itemRepository.searchAvailableItems(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemEntity(Long itemId) {
        return itemRepository.getItemById(itemId)
                .orElseThrow(() -> new ElementNotFoundException("Item не найден"));
    }
}