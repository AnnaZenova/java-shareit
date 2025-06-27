package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ElementNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        User booker = userService.getUserEntity(userId);
        Item item = itemService.getItemEntity(bookingDto.getItemId()); // Используем getEntity вместо getById

        validateBookingCreation(bookingDto, item, booker);

        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Создано бронирование ID: {}", savedBooking.getId());
        return enrichBookingDto(savedBooking);
    }

    private void validateBookingCreation(BookingDto bookingDto, Item item, User booker) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала должна быть раньше даты окончания");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Предмет недоступен для бронирования");
        }
        if (item.getOwner().getId().equals(booker.getId())) {
            throw new ValidationException("Владелец не может бронировать свой предмет");
        }
    }


    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, boolean approved) {
        log.info("Подтверждение бронирования ID: {} владельцем ID: {}", bookingId, ownerId);
        getBookingByIdOrThrow(bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking not found"));

        validateApproval(ownerId, booking);

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Бронирование ID: {} обновлено со статусом: {}", bookingId, updatedBooking.getStatus());
        return enrichBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        log.info("Запрос бронирования ID: {} пользователем ID: {}", bookingId, userId);
        getBookingByIdOrThrow(bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Бронирование не найдено"));

        validateViewAccess(userId, booking);
        log.info("Получено бронирование с ID: {} пользователя с ID: {}", bookingId, userId);
        return enrichBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String state) {
        userExists(userId);
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
        log.info("Получено бронирование пользователя ID: {} со статусом: {}", userId, state);
        return filterAndMapBookings(bookings, state);
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, String state) {
        userExists(ownerId);
        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
        log.info("Получены бронирования владельца с ID: {} со статусом: {}", ownerId, state);
        return filterAndMapBookings(bookings, state);
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

    private BookingDto enrichBookingDto(Booking booking) {
        BookingDto dto = BookingMapper.toBookingDto(booking);
        dto.setBooker(new UserDto(booking.getBooker().getId(), null, null));
        ItemDto itemDto = ItemMapper.toItemDto(booking.getItem());
        itemDto.setDescription(null);
        itemDto.setAvailable(null);
        itemDto.setOwner(null);

        dto.setItem(itemDto);
        return dto;
    }

    private Booking getBookingByIdOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ElementNotFoundException("Booking not found"));
    }

    private void validateApproval(Long ownerId, Booking booking) {
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Только owner может подтвердить бронирование");
        }
        if (booking.getStatus() != Status.WAITING) {
            throw new ValidationException("Бронирование уже подтверждено/отменено");
        }
    }

    private void validateViewAccess(Long userId, Booking booking) {
        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ValidationException("Only booker or owner can view booking");
        }
    }

    private List<BookingDto> filterAndMapBookings(List<Booking> bookings, String state) {
        return filterBookingsByState(bookings, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<Booking> filterBookingsByState(List<Booking> bookings, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state.toUpperCase()) {
            case "ALL": return bookings;
            case "CURRENT": return filterCurrentBookings(bookings, now);
            case "PAST": return filterPastBookings(bookings, now);
            case "FUTURE": return filterFutureBookings(bookings, now);
            case "WAITING": return filterByStatus(bookings, Status.WAITING);
            case "REJECTED": return filterByStatus(bookings, Status.REJECTED);
            default: throw new ValidationException("Unknown state: " + state);
        }
    }

    private List<Booking> filterCurrentBookings(List<Booking> bookings, LocalDateTime now) {
        return bookings.stream()
                .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                .collect(Collectors.toList());
    }

    private List<Booking> filterPastBookings(List<Booking> bookings, LocalDateTime now) {
        return bookings.stream()
                .filter(b -> b.getEnd().isBefore(now))
                .collect(Collectors.toList());
    }

    private List<Booking> filterFutureBookings(List<Booking> bookings, LocalDateTime now) {
        return bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .collect(Collectors.toList());
    }

    private List<Booking> filterByStatus(List<Booking> bookings, Status status) {
        return bookings.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

}