package org.prography.pingpong.domain.room.dto;

import org.prography.pingpong.domain.room.entity.Room;
import org.prography.pingpong.domain.room.entity.enums.RoomType;
import org.springframework.data.domain.Page;

import java.util.List;

public record RoomListResDto(
        Integer totalElements,
        Integer totalPages,
        List<RoomResDto> roomList
) {
    public record RoomResDto(
            Integer id,
            String title,
            Integer hostId,
            RoomType roomType,  // SINGLE(단식), DOUBLE(복식)
            String status     // WAIT(대기), PROGRESS(진행중), FINISH(완료)
    ) {
        public static RoomResDto create(Room room) {
            return new RoomResDto(
                    room.getId(),
                    room.getTitle(),
                    room.getHost().getId(),
                    room.getRoom_type(),
                    room.getStatus().name()
            );
        }
    }

    public static RoomListResDto create(Page<Room> roomPage) {
        List<RoomResDto> roomList = roomPage.getContent().stream()
                .map(RoomResDto::create)
                .toList();

        return new RoomListResDto(
                (int) roomPage.getTotalElements(),
                roomPage.getTotalPages(),
                roomList
        );
    }
}
