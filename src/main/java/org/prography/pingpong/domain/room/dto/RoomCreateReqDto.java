package org.prography.pingpong.domain.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateReqDto(
        @NotNull
        Integer userId,
        @NotBlank
        String roomType,
        @NotBlank
        String title) {
}
