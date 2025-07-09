package ru.practicum.shareit.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final long TEST_USER_ID = 1L;
    private static final long TEST_BOOKING_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = new BookingDto();
        bookingDto.setId(TEST_BOOKING_ID);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(1L);
        bookingDto.setBookerId(TEST_USER_ID);
    }

    @Test
    void createBooking_ShouldReturnBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), any(BookingDto.class)))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_BOOKING_ID));
    }

    @Test
    void approveBooking_ShouldReturnBooking() throws Exception {
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", TEST_BOOKING_ID)
                        .header(USER_ID_HEADER, TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_BOOKING_ID));
    }

    @Test
    void getBooking_ShouldReturnBooking() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", TEST_BOOKING_ID)
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_BOOKING_ID));
    }

    @Test
    void getUserBookings_ShouldReturnBookingsList() throws Exception {
        when(bookingService.getUserBookings(anyLong(), anyString()))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings?state=ALL")
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TEST_BOOKING_ID));
    }

    @Test
    void getOwnerBookings_ShouldReturnBookingsList() throws Exception {
        when(bookingService.getOwnerBookings(anyLong(), anyString()))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner?state=ALL")
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TEST_BOOKING_ID));
    }
}