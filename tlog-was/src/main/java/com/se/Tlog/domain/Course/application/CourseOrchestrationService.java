package com.se.Tlog.domain.Course.application;

import com.se.Tlog.domain.Course.controller.dto.*;
import com.se.Tlog.domain.Course.domain.Course;
import com.se.Tlog.domain.Course.domain.CourseDate;
import com.se.Tlog.domain.Course.domain.OwnerType;
import com.se.Tlog.domain.Course.repository.mongo.CourseMongoRepository;
import com.se.Tlog.domain.Search.repository.api.AiRecApi;
import com.se.Tlog.domain.Search.repository.dto.AiDestinationRes;
import com.se.Tlog.domain.Team.repository.jpa.TeamUserRepository;
import com.se.Tlog.domain.Travel.application.CustomTagService;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistDestinationRes;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CourseOrchestrationService {

    private final AiRecApi aiRecApi;
    private final TeamUserRepository teamUserRepository;
    private final CourseMongoRepository courseMongoRepository; // DB 저장용
    private final DestinationRepository destinationRepository;
    private final int TAG_LIMIT = 3;
    private final CustomTagService customTagService;

    /**
     * 내부 데이터 전달용 Record
     */
    private record CombinedDestinations(
            List<WishlistDestinationRes> wishlist,
            List<AiDestinationRes.AiDestination> aiDestinations
    ) {}

    public Map<String, List<RecommendedDestinationDto>> getCourseDetail(String courseId) {

        Course course = courseMongoRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(ErrorType.COURSE_NOT_FOUND));

        Set<String> allDestinationIds = course.getDates().stream()
                .flatMap(courseDate -> courseDate.getDestinationsIds().stream())
                .collect(Collectors.toSet());

        List<Destination> destinations = destinationRepository.findAllById(allDestinationIds);

        Map<String, List<TagCount>> topTagsMap = customTagService.getAllTopTags(
                new ArrayList<>(allDestinationIds),
                TAG_LIMIT
        );

        List<RecommendedDestinationDto> recommendedDestinations = destinations.stream()
                .map(dest -> {
                    List<TagCount> tagCountList = topTagsMap.getOrDefault(dest.getId(), List.of());
                    return RecommendedDestinationDto.fromDestinationDetail(dest, tagCountList);
                })
                .toList();

        Map<String, List<RecommendedDestinationDto>> groupedDestinations = recommendedDestinations.stream()
                .collect(Collectors.groupingBy(
                        dest -> (dest.district() != null ? dest.district() : "기타 지역"),
                        Collectors.toList()
                ));

        return groupedDestinations;
    }

    // 코스 추천 미리 보기
    @Transactional(readOnly = true)
    public Map<String, List<RecommendedDestinationDto>> getCourseRecommendations(UUID ownerId, OwnerType ownerType, CourseRecommendationReq request) {

        CombinedDestinations combined = fetchAllDestinations(ownerId, ownerType, request);

        Stream<RecommendedDestinationDto> aiStream = combined.aiDestinations().stream()
                .map(RecommendedDestinationDto::fromAiDestination);

        Stream<RecommendedDestinationDto> wishlistStream = combined.wishlist().stream()
                .map(RecommendedDestinationDto::fromWishlistDestination);

        return groupDestinationsByDistrict(Stream.concat(aiStream, wishlistStream));
    }

    @Transactional
    public String saveCourse(UUID ownerId, OwnerType ownerType, CourseSaveReq request) {

        // 1. CourseDate 리스트 생성 (프론트에서 준 ID 그대로 사용)
        List<CourseDate> courseDates = request.dailySchedules().stream()
                .map(schedule -> CourseDate.create(
                        schedule.dayNumber(),
                        schedule.destinationIds() // 사용자가 확정한 ID 리스트
                ))
                .toList();

        Course newCourse = Course.create(ownerId, ownerType, request.startDate(), request.endDate(), courseDates);

        Course savedCourse = courseMongoRepository.save(newCourse);
        return savedCourse.getId(); // id 굳이 반환해야 하나?
    }

    // ai 호출 및 여행지 데이터 받는 메서드
    private CombinedDestinations fetchAllDestinations(UUID ownerId, OwnerType ownerType, CourseRecommendationReq request) {

        List<WishlistDestinationRes> wishlist = (request.wishlist() == null)
                ? Collections.emptyList()
                : request.wishlist();

        int totalPlaceCount = request.dailyPlans().stream()
                .mapToInt(DayPlanDto::placeCount)
                .sum();

        int placesToFetchFromAI = totalPlaceCount - wishlist.size();
        List<AiDestinationRes.AiDestination> aiDestinations = new ArrayList<>();

        if (placesToFetchFromAI > 0) {
            UUID leaderId;

            // 소유자가 팀이면 리더 ID 조회, 개인이면 본인 ID 사용
            if (ownerType == OwnerType.TEAM) {
                leaderId = teamUserRepository.findLeaderUuidByTeamId(ownerId)
                        .orElseThrow(() -> new CustomException(ErrorType.TEAM_LEADER_NOT_FOUND));
            } else {
                leaderId = ownerId;
            }

            // AI 서버 호출
            AiDestinationRes aiRecApiDestination = aiRecApi.getDestination(
                    leaderId,
                    request.region_codes(),
                    placesToFetchFromAI
            );

            // 결과 검증
            if (aiRecApiDestination != null && aiRecApiDestination.destinations() != null) {
                aiDestinations = aiRecApiDestination.destinations();
            } else {
                throw new CustomException(ErrorType.AI_RECOMMENDATION_FAILD);
            }
        }

        return new CombinedDestinations(wishlist, aiDestinations);
    }

    // 그룹화 메서드
    private Map<String, List<RecommendedDestinationDto>> groupDestinationsByDistrict(
            Stream<RecommendedDestinationDto> destinationStream) {

        return destinationStream
                .collect(Collectors.groupingBy(
                        dest -> (dest.district() != null ? dest.district() : "기타 지역"),
                        Collectors.toList()
                ));
    }

}