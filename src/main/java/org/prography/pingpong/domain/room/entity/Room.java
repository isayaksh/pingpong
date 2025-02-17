package org.prography.pingpong.domain.room.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.prography.pingpong.domain.room.entity.enums.RoomStatus;
import org.prography.pingpong.domain.room.entity.enums.RoomType;
import org.prography.pingpong.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="rooms")
@EntityListeners(AuditingEntityListener.class)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomId")
    private Integer id;

    private String title;

    @OneToOne
    @JoinColumn(name = "userId")
    private User host;

    @Enumerated(EnumType.STRING)
    private RoomType room_type;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Room() {
    }

    @Builder
    private Room(Integer id, String title, User host, RoomType room_type, RoomStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.host = host;
        this.room_type = room_type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Room create(String title, User host, RoomType room_type, RoomStatus status) {
        return Room.builder()
                .title(title)
                .host(host)
                .room_type(room_type)
                .status(status)
                .build();
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getHost() {
        return host;
    }

    public RoomType getRoom_type() {
        return room_type;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
