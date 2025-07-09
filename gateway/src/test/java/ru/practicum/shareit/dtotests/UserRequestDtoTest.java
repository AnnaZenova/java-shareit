package ru.practicum.shareit.dtotests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.UserRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerialize() throws JsonProcessingException {
        UserRequestDto dto = UserRequestDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        String json = objectMapper.writeValueAsString(dto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Test User\"");
        assertThat(json).contains("\"email\":\"test@example.com\"");
    }

    @Test
    void testDeserialize() throws JsonProcessingException {
        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserRequestDto dto = objectMapper.readValue(json, UserRequestDto.class);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test User");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
    }
}