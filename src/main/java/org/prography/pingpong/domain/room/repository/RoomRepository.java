package org.prography.pingpong.domain.room.repository;

import org.prography.pingpong.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
