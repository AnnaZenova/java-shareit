package ru.practicum.shareit.dtotests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.BookingRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws JsonProcessingException {
        BookingRequestDto dto = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 1, 1, 10, 0))
                .end(LocalDateTime.of(2023, 1, 2, 10, 0))
                .build();

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"itemId\":1");
        assertThat(json).contains("\"start\":\"2023-01-01T10:00:00\"");
        assertThat(json).contains("\"end\":\"2023-01-02T10:00:00\"");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        String json = "{\"itemId\":1,\"start\":\"2023-01-01T10:00:00\",\"end\":\"2023-01-02T10:00:00\"}";

        BookingRequestDto dto = objectMapper.readValue(json, BookingRequestDto.class);

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2023, 1, 2, 10, 0));
    }
}