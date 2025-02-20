package org.prography.pingpong.domain.room.service;

import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.room.entity.Room;
import org.prography.pingpong.domain.room.entity.enums.RoomStatus;
import org.prography.pingpong.domain.room.repository.RoomRepository;
import org.prography.pingpong.domain.room.repository.UserRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomStatusService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    @Transactional
    public void updateRoomStatus(Room room, RoomStatus roomStatus) {
        room.changeStatus(roomStatus);
        roomRepository.save(room);
        userRoomRepository.deleteByRoomId(room.getId());  // DELETE도 트랜잭션 내에서 실행
    }
}

