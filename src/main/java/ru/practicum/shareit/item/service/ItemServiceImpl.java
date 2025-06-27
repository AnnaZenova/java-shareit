package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ElementNotFoundException;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemDto addItem(Long userId, @Valid ItemDto itemDto) {
        User owner = userService.getUserEntity(userId);
        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);
        if (item.getAvailable() == null) {
            item.setAvailable(false);
        }
        log.info("Добавлена вещь: {} пользователю с ID: {}", itemDto, userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, @Valid ItemDto itemDto) {
        checkUserById(userId);
        Item item = getItemEntity(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new ElementNotFoundException("Вещь не принадлежит пользователю");
        }

        updateItemFields(item, itemDto);
        log.info("Обновлена вещь с ID: {}", itemId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long id) {
        Item item = getItemEntity(id);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        // Заменяем вызовы bookingService на прямые запросы к репозиторию
        itemDto.setLastBooking(getLastBooking(id));
        itemDto.setNextBooking(getNextBooking(id));
        itemDto.setComments(getItemComments(id));

        log.info("Запрошена вещь с ID: {}", id);
        return itemDto;
    }


    private void checkUserById(long userId) {
        if (UserMapper.toUser(userService.getUserById(userId)).equals(null)) {
            throw new ElementNotFoundException(String.format("пользователь с ID :{} не найден", userId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> getUserItems(Long userId) {
        userService.getUserEntity(userId); // Проверка существования пользователя
        log.info("Получены вещи пользователя с ID: {}", userId);
        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> {
                    ItemDto dto = ItemMapper.toItemDto(item);
                    dto.setLastBooking(getLastBooking(item.getId()));
                    dto.setNextBooking(getNextBooking(item.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> searchAvailableItems(String text) {
        log.info("Поиск вещей по запросу: {}", text);
        return itemRepository.searchAvailableItems(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemEntity(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ElementNotFoundException("Item не найден"));
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = userService.getUserEntity(userId);
        Item item = getItemEntity(itemId);

        LocalDateTime checkTime = LocalDateTime.now().minusSeconds(1);

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, checkTime)) {
            throw new ValidationException("Пользователь не брал эту вещь в аренду");
        }

        Comment comment = CommentMapper.toComment(commentDto, item, author);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private void updateItemFields(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
    }

    private List<CommentDto> getItemComments(Long itemId) {
        return commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private BookingShortDto getLastBooking(Long itemId) {
        return bookingRepository.findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
                        itemId,
                        LocalDateTime.now(),
                        Status.APPROVED)
                .map(booking -> new BookingShortDto(booking.getId(), booking.getBooker().getId()))
                .orElse(null);
    }

    private BookingShortDto getNextBooking(Long itemId) {
        return bookingRepository.findFirstByItemIdAndStartAfterAndStatusIn(
                        itemId,
                        LocalDateTime.now(),
                        List.of(Status.APPROVED))
                .map(booking -> new BookingShortDto(booking.getId(), booking.getBooker().getId()))
                .orElse(null);
    }

}