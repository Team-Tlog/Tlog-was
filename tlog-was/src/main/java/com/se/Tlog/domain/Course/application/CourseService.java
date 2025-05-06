package com.se.Tlog.domain.Course.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Course.controller.dto.CourseDateDto;
import com.se.Tlog.domain.Course.controller.dto.CourseResponseDto;
import com.se.Tlog.domain.Course.controller.dto.CreateCourseRequestDto;
import com.se.Tlog.domain.Course.domain.Course;
import com.se.Tlog.domain.Course.domain.CourseDate;
import com.se.Tlog.domain.Course.domain.OwnerType;
import com.se.Tlog.domain.Course.repository.mongo.CourseRepository;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Travel.application.CustomTagService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.domain.Travel.domain.TagCount;
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
    private final CustomTagService customTagService; // Destination을 조회하는데 Custom Tag를 별도로 참조해야 하는 구조는 추후 수정 예정
    private final CourseRepository courseRepository;
    
    private void validateOwner(UUID ownerId, OwnerType ownerType) {
        if (ownerType == OwnerType.USER)
            if (!userRepository.existsById(ownerId))
                throw new CustomException(ErrorType.USER_NOT_FOUND);
        if (ownerType == OwnerType.TEAM)
            if (!teamRepository.existsById(ownerId))
                throw new CustomException(ErrorType.TEAM_NOT_FOUND);
    }
    
    /**
     * 코스와 관련된 모든 여행지 정보를 <여행지id, dto> 맵으로 반환합니다.
     * @param courses
     * @return
     */
    private Map<String, DestinationSummaryRes> getAllDestinationByCourse(List<Course> courses) {
        return destinationRepository.findAllById(
                // course에서 요구하는 모든 여행지 id 수집
                courses.stream()
                .flatMap(course -> course.getDates().stream())
                .flatMap(date -> date.getDestinations().stream())
                .distinct()
                .collect(Collectors.toList())
        ).stream()
        .map(destination -> {
            // 여행지 -> dto 변환
            // 쿼리를 일일이 요청하게 되어, 성능 상 문제가 될 수 있음. (이 구조를 사용하는 모든 부분을 포함해 점검할 것!)
            List<TagCount> topTags = customTagService.getTopTags(destination.getId(), 3);
            return DestinationSummaryRes.from(destination, topTags);
        }).collect(Collectors.toMap(DestinationSummaryRes::id, d -> d));
    }
    
    /**
     * 여행 코스 정보를 조회합니다.
     * @param ownerId
     * @param ownerType
     * @return
     */
    public List<CourseResponseDto> getCourseList(UUID ownerId, OwnerType ownerType) {
        validateOwner(ownerId, ownerType);
        
        // DB Read
        List<Course> courses = courseRepository.findAllByOwnerIdAndOwnerType(ownerId, ownerType);
        Map<String, DestinationSummaryRes> destinations = getAllDestinationByCourse(courses);
        
        // DB Document -> DTO
        List<CourseResponseDto> courseResponses = new ArrayList<CourseResponseDto>();
        for (Course course : courses) {
            List<CourseDateDto> dateDtos = new ArrayList<CourseDateDto>();
            for (CourseDate date : course.getDates()) {
                // 날짜별로 여행지를 표시할 때, 지역 단위로 그룹화됩니다.
                dateDtos.add(CourseDateDto.groupByCity(
                        date.getDestinations()
                        .stream()
                        .map(destinations::get)
                        .collect(Collectors.toList())));
            }
            courseResponses.add(CourseResponseDto.from(course, dateDtos));
        }
        
        return courseResponses;
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
