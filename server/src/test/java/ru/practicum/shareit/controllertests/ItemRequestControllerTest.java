package ru.practicum.shareit.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final long TEST_USER_ID = 1L;
    private static final long TEST_REQUEST_ID = 1L;
    private static final String TEST_DESCRIPTION = "Нужна дрель";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto requestDto;
    private ItemRequestWithItemsDto requestWithItemsDto;

    @BeforeEach
    void setUp() {
        requestDto = new ItemRequestDto();
        requestDto.setId(TEST_REQUEST_ID);
        requestDto.setDescription(TEST_DESCRIPTION);
        requestDto.setCreated(LocalDateTime.now());

        requestWithItemsDto = new ItemRequestWithItemsDto();
        requestWithItemsDto.setId(TEST_REQUEST_ID);
        requestWithItemsDto.setDescription(TEST_DESCRIPTION);
        requestWithItemsDto.setCreated(LocalDateTime.now());
    }

    @Test
    void createRequest_ShouldReturnCreatedRequest() throws Exception {
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getUserRequests_ShouldReturnRequestsList() throws Exception {
        when(itemRequestService.getUserRequests(anyLong()))
                .thenReturn(Collections.singletonList(requestWithItemsDto));

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_ShouldReturnRequestsList() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(requestWithItemsDto));

        mockMvc.perform(get("/requests/all?from=0&size=10")
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_ShouldReturnRequest() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestWithItemsDto);

        mockMvc.perform(get("/requests/{requestId}", TEST_REQUEST_ID)
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk());
    }
}