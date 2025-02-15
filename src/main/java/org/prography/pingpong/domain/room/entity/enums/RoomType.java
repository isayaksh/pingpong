package org.prography.pingpong.domain.room.entity.enums;

import lombok.Getter;

@Getter
public enum RoomType {
    SINGLE("단식"),
    DOUBLE("복식");

    private final String description;

    RoomType(String description) {
        this.description = description;
    }
}
