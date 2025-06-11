package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ElementNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        log.info("Создание бронирования для пользователя ID: {}", userId);
        User booker = userService.getUserEntity(userId);
        Item item = itemService.getItemEntity(bookingDto.getItem());

        if (!item.getAvailable()) {
            throw new ElementNotFoundException("Item недоступен к бронированию");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new ValidationException("Owner не может забронировать");
        }

        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Создано бронирование ID: {}", savedBooking.getId());
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, boolean approved) {
        log.info("Подтверждение бронирования ID: {} владельцем ID: {}", bookingId, ownerId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking не найдено"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Только owner может подтвердить бронирование");
        }

        if (booking.getStatus() != Status.WAITING) {
            throw new ValidationException("Бронирование уже подтверждено/отменено");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Бронирование ID: {} обновлено со статусом: {}", bookingId, updatedBooking.getStatus());
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        log.info("Запрос бронирования ID: {} пользователем ID: {}", bookingId, userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Бронирование не найдено"));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ValidationException("Only booker or owner can view booking");
        }
        log.info("Получено бронирование с ID: {} пользователя с ID: {}", bookingId, userId);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String state) {
        userExists(userId);
        List<Booking> bookings = bookingRepository.findByBookerId(userId);
        log.info("Получено бронирование пользователя ID: {} со статусом: {}", userId, state);
        return filterBookingsByState(bookings, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, String state) {
        userExists(ownerId);
        List<Booking> bookings = bookingRepository.findByItemOwnerId(ownerId);
        log.info("Получены бронирования владельца с ID: {} со статусом: {}", ownerId, state);
        return filterBookingsByState(bookings, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void userExists(Long userId) {
        try {
            userService.getUserEntity(userId);
            log.debug("Пользователь с ID: {} существует", userId);
        } catch (ElementNotFoundException ex) {
            log.error("Пользователь с ID: {} не найден", userId);
            throw ex;
        }
    }

    private List<Booking> filterBookingsByState(List<Booking> bookings, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state.toUpperCase()) {
            case "ALL":
                return bookings;
            case "CURRENT":
                return bookings.stream()
                        .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                        .collect(Collectors.toList());
            case "PAST":
                return bookings.stream()
                        .filter(b -> b.getEnd().isBefore(now))
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookings.stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .collect(Collectors.toList());
            case "WAITING":
                return bookings.stream()
                        .filter(b -> b.getStatus() == Status.WAITING)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookings.stream()
                        .filter(b -> b.getStatus() == Status.REJECTED)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }
}