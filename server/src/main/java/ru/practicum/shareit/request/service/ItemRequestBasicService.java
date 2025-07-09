package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;

public interface ItemRequestBasicService {
    ItemRequest findById(Long requestId);
}
