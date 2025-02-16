package org.prography.pingpong.global.init.service;

import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.room.repository.RoomRepository;
import org.prography.pingpong.domain.user.entity.User;
import org.prography.pingpong.domain.user.repository.UserRepository;
import org.prography.pingpong.global.init.dto.FakerApiResDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InitService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public void init(Integer seed, Integer quantity) throws Exception {

        /*
         * 모든 회원 정보 및 방 정보를 삭제
         */
        userRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();

        /*
         * 외부 API 호출
         */
        FakerApiResDto fakerApiResDto = WebClient.builder().baseUrl("https://fakerapi.it").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/users")
                        .queryParam("_seed", seed)
                        .queryParam("_quantity", quantity)
                        .queryParam("_locale", "ko_KR")
                        .build()
                )
                .retrieve()
                .bodyToMono(FakerApiResDto.class)
                .block();

        /*
         * 회원 정보 저장
         */
        if(fakerApiResDto.code() != 200) throw new IllegalStateException("service");    // API 호출 실패

        List<User> users = Optional.ofNullable(fakerApiResDto.data())
                .orElse(Collections.emptyList())
                .stream()
                .map(FakerApiResDto.FakerUserDto::createUser)
                .filter(Objects::nonNull)
                .toList();

        userRepository.saveAllAndFlush(users);

    }


}
