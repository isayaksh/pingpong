package org.prography.pingpong.global.init.controller;

import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.room.service.RoomService;
import org.prography.pingpong.domain.user.entity.User;
import org.prography.pingpong.domain.user.service.UserService;
import org.prography.pingpong.global.common.response.ApiResponse;
import org.prography.pingpong.global.init.dto.InitReqDto;
import org.prography.pingpong.global.init.service.InitService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class InitController {

    private final InitService initService;

    @PostMapping
    public ApiResponse init(@RequestBody @Validated InitReqDto initReqDto) throws Exception {
        List<User> init = initService.init(initReqDto.seed(), initReqDto.quantity());
        return ApiResponse.success(init);
    }

}
