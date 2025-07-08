package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ElementNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemRequestResponseService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRequestResponseService itemRequestResponseService;

    @Override
    @Transactional
    public ItemRequestDto createRequest(Long userId, ItemRequestDto itemRequestDto) {
        User requester = userService.getUserEntity(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requester);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        log.info("Создан запрос с ID: {}", savedRequest.getId());
        return ItemRequestMapper.toItemRequestDto(savedRequest);
    }

    @Override
    public List<ItemRequestWithItemsDto> getUserRequests(Long userId) {
        userService.getUserEntity(userId); // Проверка существования пользователя
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
        return requests.stream()
                .map(request -> {
                    List<Item> items = itemRequestResponseService.findItemsByRequestId(request.getId());
                    return ItemRequestMapper.toItemRequestWithItemsDto(request, items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllRequests(Long userId, int from, int size) {
        userService.getUserEntity(userId);

        List<ItemRequest> allRequests = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId);

        List<ItemRequest> dividedRequests = allRequests.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());

        return dividedRequests.stream()
                .map(request -> {
                    List<Item> items = itemRequestResponseService.findItemsByRequestId(request.getId());
                    return ItemRequestMapper.toItemRequestWithItemsDto(request, items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestWithItemsDto getRequestById(Long userId, Long requestId) {
        userService.getUserEntity(userId); // Проверка существования пользователя
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ElementNotFoundException("Запрос не найден"));
        List<Item> items = itemRequestResponseService.findItemsByRequestId(requestId);
        return ItemRequestMapper.toItemRequestWithItemsDto(request, items);
    }

    @Override
    public ItemRequest findById(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ElementNotFoundException("Запрос с ID " + requestId + " не найден"));
    }
}
