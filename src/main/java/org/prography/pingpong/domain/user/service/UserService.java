package org.prography.pingpong.domain.user.service;



import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.user.repository.UserRepository;
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

}
