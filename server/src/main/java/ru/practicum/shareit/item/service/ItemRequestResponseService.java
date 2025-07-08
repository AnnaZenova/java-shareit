package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestResponseService {
    private final ItemRepository itemRepository;

    public List<Item> findItemsByRequestId(Long requestId) {
        return itemRepository.findByRequestId(requestId);
    }
}