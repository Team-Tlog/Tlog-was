package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.Entity.Destination;
import com.se.Tlog.domain.Travel.Service.DestinationService;
import com.se.Tlog.domain.Travel.dto.DestinationDto;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Destination>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable String id) {
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }
}
