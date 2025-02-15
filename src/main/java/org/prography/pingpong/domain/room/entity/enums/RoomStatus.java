package org.prography.pingpong.domain.room.entity.enums;

import lombok.Getter;

@Getter
public enum RoomStatus {
    WAIT("대기"),
    PROGRESS("진행중"),
    FINISH("완료");

    private final String description;

    RoomStatus(String description) {
        this.description = description;
    }
}
