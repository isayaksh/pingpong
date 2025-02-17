package org.prography.pingpong.domain.room.repository;

import org.prography.pingpong.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByHostId(Integer hostId);

}
