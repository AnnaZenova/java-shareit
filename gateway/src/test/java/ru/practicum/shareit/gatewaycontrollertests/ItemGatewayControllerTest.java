package ru.practicum.shareit.gatewaycontrollertests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemGatewayController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemGatewayController.class)
class ItemGatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @Test
    void addItem() throws Exception {
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Item\",\"description\":\"Description\",\"available\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Item\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getItem() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getUserItems() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchAvailableItems() throws Exception {
        mockMvc.perform(get("/items/search?text=дрель"))
                .andExpect(status().isOk());
    }

    @Test
    void addComment() throws Exception {
        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Comment\"}"))
                .andExpect(status().isOk());
    }
}