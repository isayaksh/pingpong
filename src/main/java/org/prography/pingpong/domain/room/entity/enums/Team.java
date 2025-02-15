package org.prography.pingpong.domain.room.entity.enums;

import lombok.Getter;

@Getter
public enum Team {
    RED("레드"),
    BLUE("블루");

    private final String description;

    Team(String description) {
        this.description = description;
    }

}
