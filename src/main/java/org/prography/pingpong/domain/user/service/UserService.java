package org.prography.pingpong.domain.user.service;



import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.user.dto.UserListResDto;
import org.prography.pingpong.domain.user.entity.User;
import org.prography.pingpong.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public UserListResDto findAll(Pageable pageable) {
        Page<User> userList = userRepository.findAll(pageable);
        UserListResDto userListResDto = UserListResDto.create(userList);
        return userListResDto;
    }

}
