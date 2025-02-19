package org.prography.pingpong.domain.room.service;

import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.room.dto.*;
import org.prography.pingpong.domain.room.entity.Room;
import org.prography.pingpong.domain.room.entity.UserRoom;
import org.prography.pingpong.domain.room.entity.enums.RoomStatus;
import org.prography.pingpong.domain.room.entity.enums.RoomType;
import org.prography.pingpong.domain.room.entity.enums.Team;
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

        //방을 생성하려고 하는 user(userId)의 상태가 활성(ACTIVE)상태일 때만, 방을 생성
        Integer userId = roomCreateReqDto.userId();
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("RoomService.createRoom"));

        if(!findUser.getStatus().equals(UserStatus.ACTIVE))
            throw new ApiException("RoomService.createRoom");

        //방을 생성하려고 하는 user(userId)가 현재 참여한 방이 있다면, 방생성 X
        if(isJoinRoom(userId))
            throw new ApiException("RoomService.createRoom");

         //방은 초기에 대기(WAIT) 상태로 생성
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

    @Transactional
    public void attendRoom(Integer roomId, RoomAttentionReqDto roomAttentionReqDto) {

        // 대기(WAIT) 상태인 방에만 참가 가능
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("RoomService.attendRoom"));
        if(!room.getStatus().equals(RoomStatus.WAIT))
            throw new ApiException("RoomService.attendRoom");

        // 유저(userId)가 활성(ACTIVE) 상태일 때만, 방에 참가 가능
        User user = userRepository.findById(roomAttentionReqDto.userId())
                .orElseThrow(() -> new ApiException("RoomService.attendRoom"));
        if(!user.getStatus().equals(UserStatus.ACTIVE))
            throw new ApiException("RoomService.attendRoom");

        //방을 생성하려고 하는 user(userId)가 현재 참여한 방이 있다면, 방생성 X
        if(isJoinRoom(roomAttentionReqDto.userId()))
            throw new ApiException("RoomService.attendRoom");

        // 참가하고자 하는 방(roomId)의 정원이 미달일 때만, 참가가 가능
        int userCount = userRoomRepository.countByRoomId(roomId);
        System.out.println("userCount : " + userCount);

        if(room.getType().equals(RoomType.SINGLE) && userCount >= 1)
            throw new ApiException("RoomService.attendRoom");

        if(room.getType().equals(RoomType.DOUBLE) && userCount >= 3)
            throw new ApiException("RoomService.attendRoom");

        // userRoom 생성
        UserRoom userRoom = UserRoom.create(room, user, generateTeam(userCount));
        userRoomRepository.save(userRoom);

    }

    @Transactional
    public void outRoom(int roomId, RoomOutReqDto roomOutReqDto) {

        // 유저(userId)가 현재 해당 방(roomId)에 참가한 상태일 때만, 나가기 가능
        if(!roomRepository.existsByIdAndHostId(roomId, roomOutReqDto.userId()) &&
                !userRoomRepository.existsByUserIdAndRoomId(roomOutReqDto.userId(), roomId))
            throw new ApiException("RoomService.outRoom");

        // 이미 시작(PROGRESS) 상태인 방이거나 끝난(FINISH) 상태의 방은 나가기 불가능
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("RoomService.outRoom"));

        if(!room.getStatus().equals(RoomStatus.WAIT))
            throw new ApiException("RoomService.outRoom");

        // 호스트가 방을 나가게 되면
        if(room.getHost().getId().equals(roomOutReqDto.userId())) {
            // 방에 있던 모든 사람도 해당 방에서 나가게 됩니다.
            userRoomRepository.deleteByRoomId(roomId);

            // 해당 방은 끝난(FINISH) 상태가 됩니다.
            room.changeStatus(RoomStatus.FINISH);
        }
        else {
            // 유저 방에서 나가기
            userRoomRepository.deleteByUserId(roomOutReqDto.userId());
        }

    }


    /*
     * 유저(userId)가 현재 참여한 방이 있는지 확인
     */
    private boolean isJoinRoom(Integer userId) {
        if(roomRepository.existsByHostId(userId) || userRoomRepository.existsByUserId(userId))
            return true;
        return false;
    }

    private Team generateTeam(Integer userCount) {
        return (userCount%2 == 0) ? Team.RED : Team.BLUE;
    }


}
