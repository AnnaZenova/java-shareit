package ru.practicum.shareit.gatewaycontrollertests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.request.ItemRequestClient;
import ru.practicum.shareit.item.request.ItemRequestGatewayController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestGatewayController.class)
class ItemRequestGatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Test
    void createRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Need item\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getUserRequests() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests() throws Exception {
        mockMvc.perform(get("/requests/all?from=0&size=10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}