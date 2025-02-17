package org.prography.pingpong.domain.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.prography.pingpong.domain.room.entity.Room;
import org.prography.pingpong.domain.room.entity.enums.RoomStatus;
import org.prography.pingpong.domain.room.entity.enums.RoomType;
import org.prography.pingpong.domain.user.entity.User;

public record RoomCreateReqDto(
        @NotNull
        Integer userId,
        @NotNull
        RoomType roomType,
        @NotBlank
        String title) {

        public Room create(User user) {
                return Room.create(title, user, roomType, RoomStatus.WAIT);
        }
}
