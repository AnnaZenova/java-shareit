package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @BeforeEach
    void setUp() {
        // Создаём только по одному пользователю каждого типа
        owner = userService.getUserEntity(userService.addUser(UserDto.builder()
                .name("Owner")
                .email("owner@example.com")
                .build()).getId());

        booker = userService.getUserEntity(userService.addUser(UserDto.builder()
                .name("Booker")
                .email("booker@example.com")
                .build()).getId());

        ItemDto itemDto = ItemDto.builder()
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

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