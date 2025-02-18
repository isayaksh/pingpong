package org.prography.pingpong.domain.room.service;

import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.room.dto.RoomCreateReqDto;
import org.prography.pingpong.domain.room.dto.RoomDetailResDto;
import org.prography.pingpong.domain.room.dto.RoomListResDto;
import org.prography.pingpong.domain.room.entity.Room;
import org.prography.pingpong.domain.room.repository.RoomRepository;
import org.prography.pingpong.domain.room.repository.UserRoomRepository;
import org.prography.pingpong.domain.user.entity.User;
import org.prography.pingpong.domain.user.entity.enums.UserStatus;
import org.prography.pingpong.domain.user.repository.UserRepository;
import org.prography.pingpong.global.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;


    @Transactional
    public void deleteAll() {
        roomRepository.deleteAll();
    }

    @Transactional
    public void createRoom(RoomCreateReqDto roomCreateReqDto) {

        /*
         * 방을 생성하려고 하는 user(userId)의 상태가 활성(ACTIVE)상태일 때만, 방을 생성
         */
        Integer userId = roomCreateReqDto.userId();
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("RoomService.createRoom"));

        if(!findUser.getStatus().equals(UserStatus.ACTIVE))
            throw new ApiException("RoomService.createRoom");

        /*
         * 방을 생성하려고 하는 user(userId)가 현재 참여한 방이 있다면, 방생성 X
         */
        if(roomRepository.existsByHostId(userId))
            throw new ApiException("RoomService.createRoom");

        if(userRoomRepository.existsByUserId(userId))
            throw new ApiException("RoomService.createRoom");

        /*
         * 방은 초기에 대기(WAIT) 상태로 생성
         */
        Room room = roomCreateReqDto.create(findUser);
        roomRepository.save(room);

    }

    public RoomListResDto findAll(Pageable pageable) {
        Page<Room> roomList = roomRepository.findAll(pageable);
        RoomListResDto roomListResDto = RoomListResDto.create(roomList);
        return roomListResDto;
    }

    public RoomDetailResDto find(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("RoomService.find"));
        return RoomDetailResDto.create(room);
    }

}
