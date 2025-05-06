package com.se.Tlog.domain.Course.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Course.controller.dto.CreateCourseRequestDto;
import com.se.Tlog.domain.Course.domain.Course;
import com.se.Tlog.domain.Course.domain.CourseDate;
import com.se.Tlog.domain.Course.domain.OwnerType;
import com.se.Tlog.domain.Course.repository.mongo.CourseRepository;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CourseService {
    private final UserRepository  userRepository;
    private final TeamRepository teamRepository;
    private final DestinationRepository destinationRepository;
    private final CourseRepository courseRepository;
    
    private void validateOwner(UUID ownerId, OwnerType ownerType) {
        if (ownerType == OwnerType.USER)
            if (!userRepository.existsById(ownerId))
                throw new CustomException(ErrorType.USER_NOT_FOUND);
        if (ownerType == OwnerType.TEAM)
            if (!teamRepository.existsById(ownerId))
                throw new CustomException(ErrorType.TEAM_NOT_FOUND);
    }
    
    public String createCourse(UUID ownerId, OwnerType ownerType, CreateCourseRequestDto request) {
        validateOwner(ownerId, ownerType);
        
        // verify existence of all destinations
        List<String> distinctDestinationIds = request.destinationIds().stream()
            .flatMap(desList -> desList.stream())
            .distinct()
            .toList();
        if (distinctDestinationIds.size()
            != destinationRepository.findAllById(distinctDestinationIds).size())
            throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
        
        Course newCourse = Course.create( 
                ownerId,
                ownerType,
                request.startDate(),
                request.endDate(),
                request.destinationIds().stream()
                    .map(desList -> CourseDate.create(desList))
                    .collect(Collectors.toList()));
        courseRepository.save(newCourse);
        return newCourse.getId();
    }
}
