package org.prography.pingpong.domain.user.controller;


import org.prography.pingpong.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public ApiResponse user(@RequestParam("size") int size, @RequestParam("page") int page) {
        return ApiResponse.success();
    }

}
