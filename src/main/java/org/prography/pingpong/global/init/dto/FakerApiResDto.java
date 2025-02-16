package org.prography.pingpong.global.init.dto;

import org.prography.pingpong.domain.user.entity.User;
import org.prography.pingpong.domain.user.entity.enums.UserStatus;

import java.util.List;

public record FakerApiResDto(
        String status,
        Integer code,
        String locale,
        String seed,
        Integer total,
        List<FakerUserDto> data
) {
    public record FakerUserDto(
            Integer id,
            String uuid,
            String firstname,
            String lastname,
            String username,
            String password,
            String email,
            String ip,
            String macAddress,
            String website,
            String image
    ) {
        public User createUser() {
            return User.create(id, username, email, UserStatus.fromFakerId(id));
        }
    }
}
