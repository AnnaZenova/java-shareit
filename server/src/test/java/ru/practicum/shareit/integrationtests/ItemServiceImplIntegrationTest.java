package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    private User owner;
    private ItemDto itemDto;
    private ItemDto updateDto;

    @BeforeEach
    void setUp() {
        owner = UserMapper.toUser(userService.addUser(UserDto.builder()
                .name("Owner")
                .email("owner@example.com")
                .build()));

        itemDto = ItemDto.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .build();

        updateDto = ItemDto.builder()
                .name("Updated Item")
                .build();
    }

    @Test
    void addItem_shouldCreateItem() {
        ItemDto created = itemService.addItem(owner.getId(), itemDto);

        assertNotNull(created.getId());
        assertEquals("Item", created.getName());
        assertEquals(owner.getId(), created.getOwner());
    }

    @Test
    void updateItem_shouldUpdateItem() {
        ItemDto created = itemService.addItem(owner.getId(), itemDto);
        ItemDto updated = itemService.updateItem(owner.getId(), created.getId(), updateDto);

        assertEquals("Updated Item", updated.getName());
        assertEquals(created.getDescription(), updated.getDescription());
    }

    @Test
    void getItemById_shouldReturnItem() {
        ItemDto created = itemService.addItem(owner.getId(), itemDto);
        ItemDto found = itemService.getItemById(created.getId(), owner.getId());

        assertEquals(created.getId(), found.getId());
    }
}