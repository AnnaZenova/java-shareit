package ru.practicum.shareit.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto createTestRequestDto() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Нужна дрель");
        requestDto.setCreated(LocalDateTime.now());
        return requestDto;
    }

    private ItemRequestWithItemsDto createTestRequestWithItemsDto() {
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto();
        dto.setId(1L);
        dto.setDescription("Нужна дрель");
        dto.setCreated(LocalDateTime.now());
        return dto;
    }

    @Test
    void createRequest_ShouldReturnCreatedRequest() throws Exception {
        ItemRequestDto requestDto = createTestRequestDto();
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getUserRequests_ShouldReturnRequestsList() throws Exception {
        ItemRequestWithItemsDto requestWithItemsDto = createTestRequestWithItemsDto();
        ItemRequestDto requestDto = createTestRequestDto();
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);
        when(itemRequestService.getUserRequests(anyLong()))
                .thenReturn(Collections.singletonList(requestWithItemsDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_ShouldReturnRequestsList() throws Exception {
        ItemRequestWithItemsDto requestWithItemsDto = createTestRequestWithItemsDto();
        ItemRequestDto requestDto = createTestRequestDto();
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);
        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(requestWithItemsDto));

        mockMvc.perform(get("/requests/all?from=0&size=10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_ShouldReturnRequest() throws Exception {
        ItemRequestWithItemsDto requestWithItemsDto = createTestRequestWithItemsDto();
        ItemRequestDto requestDto = createTestRequestDto();
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);
        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestWithItemsDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}
