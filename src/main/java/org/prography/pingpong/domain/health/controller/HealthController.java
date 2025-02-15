package org.prography.pingpong.domain.health.controller;

import org.prography.pingpong.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse health() {
        return ApiResponse.success();
    }

}
