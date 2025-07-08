package ru.practicum.shareit.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto createTestUserDto() {
        return UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void addUser_ShouldReturnUser() throws Exception {
        UserDto userDto = createTestUserDto();
        when(userService.addUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserDto userDto = createTestUserDto();
        when(userService.getUserById(1L))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserDto userDto = createTestUserDto();
        when(userService.updateUser(eq(1L), any(UserDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    void removeUserById_ShouldReturnOk() throws Exception {
        doNothing()
                .when(userService)
                .removeUserById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}