package org.prography.pingpong.domain.room.controller;

import org.prography.pingpong.domain.room.dto.*;
import org.prography.pingpong.global.common.response.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {

    @PostMapping("/room")
    public ApiResponse createRoom(@RequestBody @Validated RoomCreateReqDto roomCreateReqDto) {
        return ApiResponse.success();
    }

    @GetMapping("/room")
    public ApiResponse room(@RequestParam("size") int size, @RequestParam("page") int page) {
        return ApiResponse.success();
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse roomDetail(@PathVariable("roomId") int roomId) {
        return ApiResponse.success();
    }

    @PostMapping("/room/attention/{roomId}")
    public ApiResponse attentionRoom(@PathVariable("roomId") int roomId,
                                     @RequestBody @Validated RoomAttentionReqDto roomAttentionReqDto) {
        return ApiResponse.success();
    }

    @PostMapping("/room/out/{roomId}")
    public ApiResponse outRoom(@PathVariable("roomId") int roomId,
                               @RequestBody @Validated RoomOutReqDto roomOutReqDto) {
        return ApiResponse.success();
    }

    @PutMapping("/room/start/{roomId}")
    public ApiResponse startRoom(@PathVariable("roomId") int roomId,
                                 @RequestBody @Validated RoomStartReqDto roomStartReqDto) {
        return ApiResponse.success();
    }

    @PutMapping("/team/{roomId}")
    public ApiResponse teamChange(@PathVariable("roomId") int roomId,
                                  @RequestBody @Validated TeamChangeReqDto teamChangeReqDto) {
        return ApiResponse.success();
    }

}
