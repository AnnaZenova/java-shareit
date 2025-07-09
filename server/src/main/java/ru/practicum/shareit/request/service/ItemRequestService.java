package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService extends ItemRequestBasicService {
    ItemRequestDto createRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestWithItemsDto> getUserRequests(Long userId);

    List<ItemRequestWithItemsDto> getAllRequests(Long userId, int from, int size);

    ItemRequestWithItemsDto getRequestById(Long userId, Long requestId);
}