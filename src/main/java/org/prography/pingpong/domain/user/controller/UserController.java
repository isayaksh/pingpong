package org.prography.pingpong.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.user.dto.UserListResDto;
import org.prography.pingpong.domain.user.service.UserService;
import org.prography.pingpong.global.common.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse user(@PageableDefault(sort = "id") Pageable pageable) {
        UserListResDto userListResDto = userService.findAll(pageable);
        return ApiResponse.success(userListResDto);
    }

}
