package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent(message = "Start date должна быть в настоящем или будущем")
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime start;

    @NotNull
    @Future(message = "End date должна быть в будущем")
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime end;
}