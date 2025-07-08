package ru.practicum.shareit.comment;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import ru.practicum.shareit.item.ItemRequestDto;
import ru.practicum.shareit.user.UserRequestDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private Long id;

    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(max = 1000)
    private String text;

    private ItemRequestDto itemRequestDto;

    private UserRequestDto author;
    private String authorName;

    private LocalDateTime created;
}