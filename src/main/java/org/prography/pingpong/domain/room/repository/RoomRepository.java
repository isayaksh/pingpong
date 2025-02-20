package org.prography.pingpong.domain.room.repository;

import org.prography.pingpong.domain.room.entity.Room;
import org.prography.pingpong.domain.room.entity.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByHostId(Integer hostId);

    boolean existsByIdAndHostId(Integer userId, Integer roomId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Room r WHERE r.status = :status AND r.host.id = :hostId")
    boolean existsByHostIdAndStatus(@Param("hostId") Integer hostId, @Param("status") RoomStatus status);

    boolean existsByStatus(RoomStatus status);

    Optional<Room> findByIdAndHostId(int roomId, Integer userId);
}
