package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemForRequestDto {

    private long id;

    private String name;

    private long ownerId;

}
