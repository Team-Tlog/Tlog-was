package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {
    private final DestinationService destinationService;

    @PostMapping
    public ResponseEntity<?> createDestination(@RequestBody DestinationDto destinationDto) {
        destinationService.createDestination(destinationDto);
        return ResponseEntity
                .status(SuccessType.DESTINATION_CREATED.getStatus())
                .body(SuccessRes.from(SuccessType.DESTINATION_CREATED));
    }

    @GetMapping
    public ResponseEntity<Page<DestinationRes>> getAllDestinations(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(destinationService.getAllDestinations(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationRes> getDestinationById(@PathVariable String id) {
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }
}
