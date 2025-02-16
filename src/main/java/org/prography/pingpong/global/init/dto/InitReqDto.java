package org.prography.pingpong.global.init.dto;

import jakarta.validation.constraints.NotNull;

public record InitReqDto(
        @NotNull
        Integer seed,
        @NotNull
        Integer quantity) {
}
