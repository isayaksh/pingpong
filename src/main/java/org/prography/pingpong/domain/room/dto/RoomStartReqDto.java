package org.prography.pingpong.domain.room.dto;

import jakarta.validation.constraints.NotNull;

public record RoomStartReqDto(
        @NotNull
        Integer userId
) {
}
