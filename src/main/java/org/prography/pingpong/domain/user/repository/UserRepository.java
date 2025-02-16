package org.prography.pingpong.domain.user.repository;

import org.prography.pingpong.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
