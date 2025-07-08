package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserGatewayController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Gateway: запрос всех пользователей");
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated(ValidationGroups.Create.class)
                                          @RequestBody UserRequestDto userRequestDto) {
        userRequestDto.setId(null);
        log.info("Gateway: создание нового пользователя");
        return userClient.addUser(userRequestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Gateway: запрос пользователя ID: {}", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable long userId,
                                             @Validated(ValidationGroups.Update.class)
                                             @RequestBody UserRequestDto userRequestDto) {
        log.info("Gateway: обновление пользователя ID: {}", userId);
        return userClient.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeUserById(@PathVariable long userId) {
        log.info("Gateway: удаление пользователя ID: {}", userId);
        return userClient.removeUserById(userId);
    }
}