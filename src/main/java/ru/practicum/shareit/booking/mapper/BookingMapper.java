package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto dto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker() != null ? booking.getBooker().getId() : null)
                .status(booking.getStatus())
                .build();
        if (booking.getBooker() != null) {
            dto.setBooker(new UserDto(booking.getBooker().getId(), null, null));
        }
        if (booking.getItem() != null) {
            ItemDto itemDto = ItemMapper.toItemDto(booking.getItem());
            itemDto.setDescription(null);
            itemDto.setAvailable(null);
            itemDto.setOwner(null);
            dto.setItem(itemDto);
        }

        return dto;
    }

    public Booking toBooking(BookingDto bookingDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(bookingDto.getStatus() != null ? bookingDto.getStatus() : Status.WAITING);
        return booking;
    }
}