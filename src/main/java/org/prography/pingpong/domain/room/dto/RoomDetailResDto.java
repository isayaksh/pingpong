package org.prography.pingpong.domain.room.dto;

import org.prography.pingpong.domain.room.entity.Room;
import org.prography.pingpong.domain.room.entity.enums.RoomStatus;
import org.prography.pingpong.domain.room.entity.enums.RoomType;

import java.time.format.DateTimeFormatter;

public record RoomDetailResDto(
        Integer id,
        String title,
        Integer hostId,
        RoomType roomType,  // SINGLE(단식), DOUBLE(복식)
        RoomStatus status,    // WAIT(대기), PROGRESS(진행중), FINISH(완료)
        String createdAt,
        String updatedAt
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static RoomDetailResDto create(Room room) {
        return new RoomDetailResDto(
                room.getId(),
                room.getTitle(),
                room.getHost().getId(),
                room.getType(),
                room.getStatus(),
                room.getCreatedAt().format(FORMATTER),
                room.getUpdatedAt().format(FORMATTER)
        );
    }
}
