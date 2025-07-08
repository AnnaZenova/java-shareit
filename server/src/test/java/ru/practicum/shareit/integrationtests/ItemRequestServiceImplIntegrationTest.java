package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestService requestService;

    @Autowired
    private UserService userService;

    private User requester;

    @BeforeEach
    void setUp() {
        requester = UserMapper.toUser(userService.addUser(UserDto.builder()
                .name("Requester")
                .email("requester@example.com")
                .build()));
    }

    @Test
    void createRequest_shouldCreateRequest() {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Need item")
                .build();

        ItemRequestDto created = requestService.createRequest(requester.getId(), requestDto);

        assertNotNull(created.getId());
        assertEquals("Need item", created.getDescription());
    }

    @Test
    void getRequestById_shouldReturnRequest() {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .description("Need item")
                .build();
        ItemRequestDto created = requestService.createRequest(requester.getId(), requestDto);

        var found = requestService.getRequestById(requester.getId(), created.getId());

        assertEquals(created.getId(), found.getId());
    }
}