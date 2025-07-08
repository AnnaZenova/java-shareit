package ru.practicum.shareit.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto createTestBookingDto() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        return bookingDto;
    }

    @Test
    void createBooking_ShouldReturnBooking() throws Exception {
        BookingDto bookingDto = createTestBookingDto();
        when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_ShouldReturnBooking() throws Exception {
        BookingDto bookingDto = createTestBookingDto();
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking_ShouldReturnBooking() throws Exception {
        BookingDto bookingDto = createTestBookingDto();
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getUserBookings_ShouldReturnBookingsList() throws Exception {
        BookingDto bookingDto = createTestBookingDto();
        when(bookingService.getUserBookings(anyLong(), anyString()))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerBookings_ShouldReturnBookingsList() throws Exception {
        BookingDto bookingDto = createTestBookingDto();
        when(bookingService.getOwnerBookings(anyLong(), anyString()))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}
