package com.se.Tlog.domain.Guide.service;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Guide.controller.dto.GuideDto;
import com.se.Tlog.domain.Guide.domain.Guide;
import com.se.Tlog.domain.Guide.repository.jpa.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApplicationService
@RequiredArgsConstructor
public class GuideService {
    private final GuideRepository guideRepository;

    public Page<GuideDto> getLocalGuides(double latitude, double longitude, Pageable pageable) {
        if (90.0 < Math.abs(latitude)) latitude = 360.0;
        if (180.0 < Math.abs(longitude)) longitude = 360.0;

        Page<Guide> localguides = guideRepository.findLocalGuide(latitude, longitude, pageable);
        return localguides.map(GuideDto::from);
    }
}
