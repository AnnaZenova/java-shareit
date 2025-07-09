package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestResponseServiceImpl implements ItemRequestResponseService{
    private final ItemRepository itemRepository;

    @Override
    public List<Item> findItemsByRequestId(Long requestId) {
        log.info("Поиск предметов по ID запроса: {}", requestId);
        return itemRepository.findByRequestId(requestId);
    }
}