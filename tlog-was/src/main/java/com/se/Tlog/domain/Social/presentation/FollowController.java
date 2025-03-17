package com.se.Tlog.domain.Social.presentation;

import com.se.Tlog.domain.Social.application.FollowService;
import com.se.Tlog.domain.Social.presentation.dto.FollowDto;
import com.se.Tlog.domain.Social.presentation.dto.FollowStatusDto;
import com.se.Tlog.global.response.success.SuccessRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;


    @PostMapping
    public ResponseEntity<?> follow(@RequestBody FollowDto followDto) {
        FollowStatusDto followStatusDto = followService.follow(followDto);
        return ResponseEntity.ok()
                .body(SuccessRes.from(followStatusDto));
    }


    // 내가 팔로우한 사람들
    @GetMapping("/following/{userId}")
    public ResponseEntity<?> getFollowingList(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(followService.getFollowingList(userId,pageable));
    }

    // 나를 팔로우하는 사람들
    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getFollowerList(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(followService.getFollowerList(userId,pageable));
    }

}
