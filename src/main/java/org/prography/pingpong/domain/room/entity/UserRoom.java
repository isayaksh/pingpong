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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "roomId")
    private Room room;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private Team team;

    public UserRoom() {
    }

    protected UserRoom(Room room, User user, Team team) {
        this.room = room;
        this.user = user;
        this.team = team;
    }

    public static UserRoom create(Room room, User user, Team team) {
        return new UserRoom(room, user, team);
    }

    public void changeTeam() {
        this.team = this.team.equals(Team.RED)? Team.BLUE : Team.RED;
    }

    public Integer getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public Team getTeam() {
        return team;
    }
}
