package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public ItemDto addItem(Long userId,@Valid ItemDto itemDto) {
        checkUserById(userId);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(itemDto,owner);
        item.setOwner(owner);
        if (item.getAvailable() == null) {
            item.setAvailable(false); // или true, по умолчанию
        }
        log.info("Добавлена вещь :{} пользователю с ID :{}", itemDto, userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId,@Valid ItemDto itemDto) {
        checkUserById(userId);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(getItemById(itemId),owner);
        if (!item.getOwner().getId().equals(userId)) {
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
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(String.format("вещь с ID :{}", id)));
        log.info("Запрошена вещь с id:{}", id);

        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setLastBooking(getLastBooking(item.getId()));
        itemDto.setNextBooking(getNextBooking(item.getId()));
        itemDto.setComments(commentRepository.findByItemId(id).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

        return itemDto;
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
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ElementNotFoundException("Item не найден"));
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = userService.getUserEntity(userId);
        Item item = getItemEntity(itemId);

        // Проверка, что пользователь брал вещь в аренду
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())) {
            throw new ValidationException("Пользователь не брал эту вещь в аренду или аренда еще не завершена");
        }

        Comment comment = CommentMapper.toComment(commentDto, item, author);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private BookingShortDto getLastBooking(Long itemId) {
        return bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(
                        itemId,
                        LocalDateTime.now().minusSeconds(5)
                )
                .map(booking -> BookingShortDto.builder()
                        .id(booking.getId())
                        .bookerId(booking.getBooker().getId())
                        .build())
                .orElse(null);
    }

    private BookingShortDto getNextBooking(Long itemId) {
        // Получаем ближайшее будущее бронирование
        return bookingRepository.findFirstByItemIdAndStartAfterAndStatusIn(
                        itemId,
                        LocalDateTime.now(),
                        List.of(Status.APPROVED) // Только подтвержденные
                )
                .map(booking -> BookingShortDto.builder()
                        .id(booking.getId())
                        .bookerId(booking.getBooker().getId())
                        .build())
                .orElse(null);
    }
}