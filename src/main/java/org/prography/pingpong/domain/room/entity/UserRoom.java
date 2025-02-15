package org.prography.pingpong.domain.room.entity;

import jakarta.persistence.*;
import org.prography.pingpong.domain.room.entity.enums.Team;
import org.prography.pingpong.domain.user.entity.User;

@Entity
@Table(name="user_rooms")
public class UserRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userRoomId")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "roomId")
    private Room room_id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user_id;

    @Enumerated(EnumType.STRING)
    private Team team;
}
