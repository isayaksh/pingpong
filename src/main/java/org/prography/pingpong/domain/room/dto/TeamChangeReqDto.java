package org.prography.pingpong.domain.room.dto;

import jakarta.validation.constraints.NotNull;

public record TeamChangeReqDto(
        @NotNull
        Integer userId
) {
}
