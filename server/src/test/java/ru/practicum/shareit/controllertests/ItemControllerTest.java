package ru.practicum.shareit.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final long TEST_USER_ID = 1L;
    private static final long TEST_ITEM_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(TEST_ITEM_ID);
        itemDto.setName("Дрель");
        itemDto.setDescription("Простая дрель");
        itemDto.setAvailable(true);
    }

    @Test
    void addItem_ShouldReturnItem() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header(USER_ID_HEADER, TEST_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getItem_ShouldReturnItem() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getUserItems_ShouldReturnItemsList() throws Exception {
        when(itemService.getUserItems(anyLong()))
                .thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, TEST_USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void searchAvailableItems_ShouldReturnItemsList() throws Exception {
        when(itemService.searchAvailableItems(anyString()))
                .thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items/search?text=дрель"))
                .andExpect(status().isOk());
    }
}