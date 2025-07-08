package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private User owner;
    private User booker;
    private Item item;

    private User createTestUser(String name, String email) {
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .build();
        Long userId = userService.addUser(userDto).getId();
        return userService.getUserEntity(userId);
    }

    private ItemDto createTestItemDto(User owner, String name, String description) {
        return ItemDto.builder()
                .name(name)
                .description(description)
                .available(true)
                .build();
    }

    @BeforeEach

    void setUp() {
        owner = userService.getUserEntity(userService.addUser(UserDto.builder()
                .name("Owner")
                .email("owner@example.com")
                .build()).getId());

        booker = userService.getUserEntity(userService.addUser(UserDto.builder()
                .name("Booker")
                .email("booker@example.com")
                .build()).getId());

        owner = createTestUser("Owner_" + UUID.randomUUID(), "owner_" + UUID.randomUUID() + "@example.com");
        booker = createTestUser("Booker_" + UUID.randomUUID(), "booker_" + UUID.randomUUID() + "@example.com");

        ItemDto itemDto = createTestItemDto(owner, "Item", "Description");
        item = itemService.getItemEntity(itemService.addItem(owner.getId(), itemDto).getId());
    }

    @Test
    void createBooking_shouldCreateBooking() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDto created = bookingService.createBooking(booker.getId(), bookingDto);

        assertNotNull(created.getId());
        assertEquals(Status.WAITING, created.getStatus());
        assertEquals(item.getId(), created.getItem().getId());
        assertEquals(booker.getId(), created.getBooker().getId());
    }

    @Test
    void approveBooking_shouldApproveBooking() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingDto created = bookingService.createBooking(booker.getId(), bookingDto);

        BookingDto approved = bookingService.approveBooking(owner.getId(), created.getId(), true);

        assertEquals(Status.APPROVED, approved.getStatus());
    }

    @Test
    void getBookingById_shouldReturnBooking() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingDto created = bookingService.createBooking(booker.getId(), bookingDto);

        BookingDto found = bookingService.getBookingById(booker.getId(), created.getId());

        assertEquals(created.getId(), found.getId());
    }
}