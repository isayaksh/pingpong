package org.prography.pingpong.domain.room.repository;

import org.prography.pingpong.domain.room.entity.UserRoom;
import org.prography.pingpong.domain.room.entity.enums.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {

    boolean existsByUserId(Integer userId);

    boolean existsByUserIdAndRoomId(Integer userId, Integer roomId);

    int countByRoomId(Integer roomId);

    void deleteByRoomId(Integer roomId);

    void deleteByUserId(Integer userId);

    List<UserRoom> findByRoomId(Integer userId);

    Optional<UserRoom> findByUserId(Integer userId);

}
