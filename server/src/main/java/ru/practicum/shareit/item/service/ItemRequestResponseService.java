package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemRequestResponseService {
    List<Item> findItemsByRequestId(Long requestId);
}