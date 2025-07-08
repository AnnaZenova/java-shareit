package ru.practicum.shareit.dtotests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws JsonProcessingException {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .requestId(2L)
                .build();

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Test Item\"");
        assertThat(json).contains("\"description\":\"Test Description\"");
        assertThat(json).contains("\"available\":true");
        assertThat(json).contains("\"requestId\":2");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        String json = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"requestId\":2}";

        ItemRequestDto dto = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test Item");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getRequestId()).isEqualTo(2L);
    }
}