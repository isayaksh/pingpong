package org.prography.pingpong.domain.room.controller;

import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.room.dto.*;
import org.prography.pingpong.domain.room.entity.UserRoom;
import org.prography.pingpong.domain.room.service.RoomService;
import org.prography.pingpong.global.common.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room")
    public ApiResponse createRoom(@RequestBody @Validated RoomCreateReqDto roomCreateReqDto) {
        roomService.createRoom(roomCreateReqDto);
        return ApiResponse.success();
    }

    @GetMapping("/room")
    public ApiResponse room(Pageable pageable) {
        RoomListResDto roomListResDto = roomService.findAll(pageable);
        return ApiResponse.success(roomListResDto);
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse roomDetail(@PathVariable("roomId") int roomId) {
        RoomDetailResDto roomDetailResDto = roomService.find(roomId);
        return ApiResponse.success(roomDetailResDto);
    }

    @PostMapping("/room/attention/{roomId}")
    public ApiResponse attentionRoom(@PathVariable("roomId") int roomId,
                                     @RequestBody @Validated RoomAttentionReqDto roomAttentionReqDto) {
        roomService.attendRoom(roomId, roomAttentionReqDto);
        return ApiResponse.success();
    }

    @PostMapping("/room/out/{roomId}")
    public ApiResponse outRoom(@PathVariable("roomId") int roomId,
                               @RequestBody @Validated RoomOutReqDto roomOutReqDto) {
        roomService.outRoom(roomId, roomOutReqDto);
        return ApiResponse.success();
    }

    @PutMapping("/room/start/{roomId}")
    public ApiResponse startRoom(@PathVariable("roomId") int roomId,
                                 @RequestBody @Validated RoomStartReqDto roomStartReqDto) {
        roomService.startRoom(roomId, roomStartReqDto);
        return ApiResponse.success();
    }

    @PutMapping("/team/{roomId}")
    public ApiResponse changeTeam(@PathVariable("roomId") int roomId,
                                  @RequestBody @Validated TeamChangeReqDto teamChangeReqDto) {
        UserRoomDto userRoomDto = roomService.changeTeam(roomId, teamChangeReqDto);
        return ApiResponse.success(userRoomDto);
    }

}
