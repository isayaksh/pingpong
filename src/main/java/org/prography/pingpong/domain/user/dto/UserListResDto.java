package org.prography.pingpong.domain.user.dto;

import org.prography.pingpong.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public record UserListResDto(
        Integer totalElements,
        Integer totalPages,
        List<UserResDto> userList
) {
    public record UserResDto(
            Integer id,
            Integer fakerId,
            String name,
            String email,
            String status
    ) {
        public static UserResDto create(User user) {
            return new UserResDto(
                    user.getId(),
                    user.getFakerId(),
                    user.getName(),
                    user.getEmail(),
                    user.getStatus().name()
            );
        }
    }

    public static UserListResDto create(Page<User> userPage) {
        List<UserResDto> userList = userPage.getContent().stream()
                .map(UserResDto::create)
                .toList();

        return new UserListResDto(
                (int) userPage.getTotalElements(),
                userPage.getTotalPages(),
                userList
        );
    }
}
