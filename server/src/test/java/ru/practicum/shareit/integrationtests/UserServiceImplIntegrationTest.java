package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    private UserDto userDto;
    private UserDto updateDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("User")
                .email("user@example.com")
                .build();

        updateDto = UserDto.builder()
                .name("Updated User")
                .build();
    }

    @Test
    void addUser_shouldCreateUser() {
        UserDto created = userService.addUser(userDto);

        assertNotNull(created.getId());
        assertEquals("User", created.getName());
    }

    @Test
    void updateUser_shouldUpdateUser() {
        UserDto created = userService.addUser(userDto);
        UserDto updated = userService.updateUser(created.getId(), updateDto);

        assertEquals("Updated User", updated.getName());
        assertEquals(created.getEmail(), updated.getEmail());
    }

    @Test
    void getUserById_shouldReturnUser() {
        UserDto created = userService.addUser(userDto);
        UserDto found = userService.getUserById(created.getId());

        assertEquals(created.getId(), found.getId());
    }
}