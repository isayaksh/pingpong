package org.prography.pingpong.domain.room.entity.enums;

import lombok.Getter;

@Getter
public enum RoomType {
    SINGLE("단식", 2),
    DOUBLE("복식", 4);

    private final String description;
    private final Integer value;

    RoomType(String description, Integer value) {
        this.description = description;
        this.value = value;
    }
}
