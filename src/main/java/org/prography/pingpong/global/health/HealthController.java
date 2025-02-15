package org.prography.pingpong.global.health;

import org.prography.pingpong.global.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ApiResponse health() {
        return ApiResponse.success();
    }

}
