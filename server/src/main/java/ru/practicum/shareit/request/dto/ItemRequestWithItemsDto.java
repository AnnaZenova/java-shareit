package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestWithItemsDto {

    private long id;

    private String description;

    private LocalDateTime created;

    private List<ItemForRequestDto> items;

}

