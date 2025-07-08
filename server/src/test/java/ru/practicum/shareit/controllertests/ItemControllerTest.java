package ru.practicum.shareit.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto createTestItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Дрель");
        itemDto.setDescription("Простая дрель");
        itemDto.setAvailable(true);
        return itemDto;
    }

    @Test
    void addItem_ShouldReturnItem() throws Exception {
        ItemDto itemDto = createTestItemDto();
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() throws Exception {
        ItemDto itemDto = createTestItemDto();
        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getItem_ShouldReturnItem() throws Exception {
        ItemDto itemDto = createTestItemDto();
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getUserItems_ShouldReturnItemsList() throws Exception {
        ItemDto itemDto = createTestItemDto();
        when(itemService.getUserItems(anyLong()))
                .thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchAvailableItems_ShouldReturnItemsList() throws Exception {
        ItemDto itemDto = createTestItemDto();
        when(itemService.searchAvailableItems(anyString()))
                .thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items/search?text=дрель"))
                .andExpect(status().isOk());
    }
}
