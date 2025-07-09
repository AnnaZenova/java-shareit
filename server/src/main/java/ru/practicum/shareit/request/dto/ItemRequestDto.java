package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    private long id;

    private String description;

    private long requester;

    private LocalDateTime created;

    public ItemRequestDto(String description, long requester, LocalDateTime created) {
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}