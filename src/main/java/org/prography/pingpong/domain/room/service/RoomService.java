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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;

    private final ThreadPoolTaskScheduler taskScheduler;
    private final RoomStatusService roomStatusService;

    @Transactional
    public void createRoom(RoomCreateReqDto roomCreateReqDto) {

        // 방을 생성하려고 하는 user(userId)의 상태가 활성(ACTIVE)상태일 때만, 방을 생성
        Integer userId = roomCreateReqDto.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("RoomService.createRoom"));

        if(!user.getStatus().equals(UserStatus.ACTIVE))
            throw new ApiException("RoomService.createRoom");

        // 방을 생성하려고 하는 user(userId)가 현재 참여한 방이 있다면, 방생성 X
        if(isJoinRoom(userId))
            throw new ApiException("RoomService.createRoom");

        // 방은 초기에 대기(WAIT) 상태로 생성
        Room room = roomCreateReqDto.create(user);
        roomRepository.save(room);

        // userRoom 생성
        UserRoom userRoom = UserRoom.create(room, user, Team.RED);
        userRoomRepository.save(userRoom);

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

        if(room.getType().equals(RoomType.SINGLE) && userCount >= 2)
            throw new ApiException("RoomService.attendRoom");

        if(room.getType().equals(RoomType.DOUBLE) && userCount >= 4)
            throw new ApiException("RoomService.attendRoom");

        // userRoom 생성
        UserRoom userRoom = UserRoom.create(room, user, generateTeam(roomId));
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

    @Transactional
    public void startRoom(int roomId, RoomStartReqDto roomStartReqDto) {
        // 호스트인 유저만 게임을 시작 가능
        Room room = roomRepository.findByIdAndHostId(roomId, roomStartReqDto.userId())
                .orElseThrow(() -> new ApiException("RoomService.startRoom"));

        // 방 정원이 방의 타입에 맞게 모두 꽉 찬 상태에서만 게임 시작
        int userCount = userRoomRepository.countByRoomId(roomId);
        if(room.getType().equals(RoomType.SINGLE) && userCount != 2) throw new ApiException("RoomService.startRoom");
        if(room.getType().equals(RoomType.DOUBLE) && userCount != 4) throw new ApiException("RoomService.startRoom");

        // 현재 방의 상태가 대기(WAIT) 상태일 때만 시작
        if(!room.getStatus().equals(RoomStatus.WAIT))
            throw new ApiException("RoomService.startRoom");

        // 방의 상태를 진행중(PROGRESS) 상태로 변경
        room.changeStatus(RoomStatus.PROGRESS);

        // 게임시작이 된 방은 1분 뒤 종료(FINISH) 상태로 변경
        scheduleRoomStatusChanged(room, RoomStatus.FINISH);

    }

    @Transactional
    public void changeTeam(int roomId, TeamChangeReqDto teamChangeReqDto) {

        // 유저(userId)가 현재 해당 방(roomId)에 참가한 상태에서만 팀 변경이 가능
        if(!isJoinRoom(teamChangeReqDto.userId()))
            throw new ApiException("RoomService.changeTeam");

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("RoomService.changeTeam"));

        UserRoom userRoom = userRoomRepository.findByUserId(teamChangeReqDto.userId())
                .orElseThrow(() -> new ApiException("RoomService.changeTeam"));

        List<UserRoomDto> userRoomList = userRoomRepository.findByRoomId(roomId)
                .stream()
                .map(UserRoomDto::create)
                .collect(Collectors.toList());

        // 변경되려는 팀의 인원이 이미 해당 방 정원의 절반과 같다면 팀이 변경되지 않고 201 응답을 반환
        if(userRoom.getTeam().equals(Team.RED)) {
            Long blueCount = userRoomList.stream().filter(ur -> ur.team() == Team.BLUE).count();
            if(blueCount >= room.getType().getValue()/2)
                throw new ApiException("RoomService.changeTeam");
        }

        // 변경되려는 팀의 인원이 이미 해당 방 정원의 절반과 같다면 팀이 변경되지 않고 201 응답을 반환
        else if(userRoom.getTeam().equals(Team.BLUE)) {
            Long redCount = userRoomList.stream().filter(ur -> ur.team() == Team.RED).count();
            if(redCount >= room.getType().getValue()/2)
                throw new ApiException("RoomService.changeTeam");
        }

        // 현재 방의 상태가 대기(WAIT) 상태일 때만 팀을 변경할 수 있습니다. 만약 그렇지 않다면 201 응답을 반환
        if(!room.getStatus().equals(RoomStatus.WAIT))
            throw new ApiException("RoomService.changeTeam");

        // 유저(userId)가 현재 속한 팀 기준 반대 팀으로 변경
        userRoom.changeTeam();

    }


    /*
     * 유저(userId)가 현재 참여한 방이 있는지 확인
     */
    private boolean isJoinRoom(Integer userId) {
        if(userRoomRepository.existsByUserId(userId) || roomRepository.existsByHostIdAndStatus(userId, RoomStatus.WAIT))
            return true;
        return false;
    }

    private Team generateTeam(Integer roomId) {
        List<UserRoomDto> userRoomList = userRoomRepository.findByRoomId(roomId)
                .stream()
                .map(UserRoomDto::create)
                .collect(Collectors.toList());

        Long redCount = userRoomList.stream().filter(ur -> ur.team() == Team.RED).count();
        Long blueCount = userRoomList.stream().filter(ur -> ur.team() == Team.BLUE).count();

        return redCount > blueCount ? Team.BLUE : Team.RED;
    }

    @Async
    private void scheduleRoomStatusChanged(Room room, RoomStatus roomStatus) {
        taskScheduler.schedule(() -> {
            roomStatusService.updateRoomStatus(room, roomStatus);
        }, Instant.now().plusSeconds(60));
    }

}
