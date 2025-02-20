package org.prography.pingpong.domain.room.dto;

import org.prography.pingpong.domain.room.entity.UserRoom;
import org.prography.pingpong.domain.room.entity.enums.Team;

public record UserRoomDto(Integer id, Integer roomId, Integer userId, Team team) {

    public static UserRoomDto create(UserRoom userRoom) {
        return new UserRoomDto(userRoom.getId(), userRoom.getRoom().getId(), userRoom.getUser().getId(), userRoom.getTeam());
    }

}
