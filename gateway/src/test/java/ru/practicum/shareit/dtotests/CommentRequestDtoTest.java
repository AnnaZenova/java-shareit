package ru.practicum.shareit.dtotests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.comment.CommentRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws JsonProcessingException {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setId(1L);
        dto.setText("Test comment");
        dto.setCreated(LocalDateTime.of(2023, 1, 1, 10, 0));

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"text\":\"Test comment\"");
        assertThat(json).contains("\"created\":\"2023-01-01T10:00:00\"");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        String json = "{\"id\":1,\"text\":\"Test comment\",\"created\":\"2023-01-01T10:00:00\"}";

        CommentRequestDto dto = objectMapper.readValue(json, CommentRequestDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getText()).isEqualTo("Test comment");
        assertThat(dto.getCreated()).isEqualTo(LocalDateTime.of(2023, 1, 1, 10, 0));
    }
}