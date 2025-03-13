package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.Service.TagService;
import com.se.Tlog.domain.Travel.dto.TagDto;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagDto tagDto) {
        tagService.createTag(tagDto);
        return ResponseEntity
                .status(SuccessType.TAG_CREATED.getStatus())
                .body(SuccessRes.from(SuccessType.TAG_CREATED));
    }

    @GetMapping
    public ResponseEntity<?> getAllTags() {
        return ResponseEntity
                .ok()
                .body(SuccessRes.from(tagService.getAllTags()));
    }
}
