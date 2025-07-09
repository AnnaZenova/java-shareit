package ru.practicum.shareit.dtotests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.request.ItemRequestGatewayDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestGatewayDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws JsonProcessingException {
        ItemRequestGatewayDto dto = new ItemRequestGatewayDto();
        dto.setDescription("Test Description");
        dto.setRequester(1L);
        dto.setCreated(LocalDateTime.of(2023, 1, 1, 10, 0));

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"description\":\"Test Description\"");
        assertThat(json).contains("\"requester\":1");
        assertThat(json).contains("\"created\":\"2023-01-01T10:00:00\"");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        String json = "{\"description\":\"Test Description\",\"requester\":1,\"created\":\"2023-01-01T10:00:00\"}";

        ItemRequestGatewayDto dto = objectMapper.readValue(json, ItemRequestGatewayDto.class);

        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getRequester()).isEqualTo(1L);
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 10, 0));
    }
}