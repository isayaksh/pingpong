package org.prography.pingpong.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.prography.pingpong.domain.user.entity.enums.UserStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer id;

    @OrderColumn
    private Integer fakerId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User() {
    }

    @Builder
    private User(Integer id, Integer fakerId, String name, String email, UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fakerId = fakerId;
        this.name = name;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(Integer fakerId, String name, String email, UserStatus status) {
        return User.builder()
                .fakerId(fakerId)
                .name(name)
                .email(email)
                .status(status)
                .build();
    }

    public Integer getId() {
        return id;
    }

    public Integer getFakerId() {
        return fakerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }



}
