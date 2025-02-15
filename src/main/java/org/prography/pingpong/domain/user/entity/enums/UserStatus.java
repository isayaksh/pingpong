package org.prography.pingpong.domain.user.entity.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    WAIT("대기"),
    ACTIVE("활성"),
    NON_ACTIVE("비활성");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }
}
