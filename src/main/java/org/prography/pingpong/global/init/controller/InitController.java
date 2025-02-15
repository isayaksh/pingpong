package org.prography.pingpong.global.init.controller;

import org.prography.pingpong.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class InitController {

    @PostMapping
    public ApiResponse init() {
        return ApiResponse.success();
    }

}
