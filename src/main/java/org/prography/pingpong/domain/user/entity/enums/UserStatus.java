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

    public static UserStatus fromFakerId(Integer fakerId) {
        if(fakerId <= 30) return ACTIVE;                    // 응답 값의 id(fakerId) 값이 30 이하의 회원은 활성(ACTIVE) 상태
        if(31 <= fakerId && fakerId <= 60) return WAIT;     // 응답 값의 id(fakerId) 값이 31 이상, 60 이하의 회원은 대기(WAIT) 상태
        return NON_ACTIVE;                // 응답 값의 id(fakerId) 값이 61 이상인 회원은 비활성(NON_ACTIVE) 상태
    }
}
