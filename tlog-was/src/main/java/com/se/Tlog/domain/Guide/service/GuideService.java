package com.se.Tlog.domain.Guide.service;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Guide.controller.dto.GuideDto;
import com.se.Tlog.domain.Guide.controller.dto.RawGuideDto;
import com.se.Tlog.domain.Guide.domain.Guide;
import com.se.Tlog.domain.Guide.repository.jpa.GuideRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
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

    public Page<RawGuideDto> getAllGuides(Pageable pageable) {
        return guideRepository.findAll(pageable).map(RawGuideDto::from);
    }

    public RawGuideDto createGuide(RawGuideDto dto) {
        Guide newGuide = Guide.create(
            dto.imageUrl(),
            dto.title(),
            dto.description(),
            dto.property(),
            dto.minLatitude(),
            dto.minLongitude(),
            dto.maxLatitude(),
            dto.maxLongitude(),
            dto.infoUrl()
        );
        return RawGuideDto.from(guideRepository.save(newGuide));
    }

    public RawGuideDto updateGuide(RawGuideDto dto) {
        Guide guide = guideRepository.findById(dto.id())
                .orElseThrow(() -> new CustomException(ErrorType.GUIDE_NOT_FOUND));

        guide.setImageUrl(dto.imageUrl());
        guide.setTitle(dto.title());
        guide.setDescription(dto.description());
        guide.setProperty(dto.property());
        guide.setMinLatitude(dto.minLatitude());
        guide.setMinLongitude(dto.minLongitude());
        guide.setMaxLatitude(dto.maxLatitude());
        guide.setMaxLongitude(dto.maxLongitude());
        guide.setInfoUrl(dto.infoUrl());

        return RawGuideDto.from(guideRepository.save(guide));
    }

    public void deleteGuideById(int id) {
        guideRepository.deleteById(id);
    }
}
