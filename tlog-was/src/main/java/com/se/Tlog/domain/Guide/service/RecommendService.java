package com.se.Tlog.domain.Guide.service;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Guide.controller.dto.BannerDto;
import com.se.Tlog.domain.Guide.controller.dto.RecommendDestDto;
import com.se.Tlog.domain.Guide.controller.dto.RecommendPostDto;
import com.se.Tlog.domain.Guide.domain.BannerType;
import com.se.Tlog.domain.Guide.domain.RecBanner;
import com.se.Tlog.domain.Guide.repository.dto.FullRecDest;
import com.se.Tlog.domain.Guide.repository.dto.RecomPost;
import com.se.Tlog.domain.Guide.repository.mongo.RecBannerRepository;
import com.se.Tlog.domain.Guide.repository.mongo.RecDestRepository;
import com.se.Tlog.domain.Guide.repository.mongo.RecPostRepository;
import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class RecommendService {
    private final DestinationService destinationService;
    private final UserRepository userRepository;

    private final RecBannerRepository bannerRepository;
    private final RecDestRepository destRepository;
    private final RecPostRepository postRepository;

    private BannerDto makeBannerDto(RecBanner banner, User user) {
        if (BannerType.JUST_INFO == banner.getBannerType() ||
            BannerType.DESTINATION_BUNDLE == banner.getBannerType())
            return BannerDto.create(banner);
        if (BannerType.TBTI_DEST_RECOMMEND == banner.getBannerType())
            // 추후 변경 계획 : 이 부분은 추천 알고리즘을 적용해, TBTI 및 계절에 따른 추천 여행지를 표시할 예정입니다.
            return BannerDto.create(banner, user.getTbtiString() + banner.getTitle());
        else
            throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
    }

    public List<BannerDto> getBanners(UUID userId) {
        List<RecBanner> banners = bannerRepository.findAll();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        return banners.stream()
                .map(ban -> makeBannerDto(ban, user))
                .toList();
    }

    public List<RecommendDestDto> getRecommendDests() {
        List<FullRecDest> dests = destRepository.findSomeWithDestinations(3);
        return dests.stream()
                .map(RecommendDestDto::create)
                .toList();
    }

    private RecommendPostDto makePostDto(RecomPost post) {
        String title = null;
        String description = null;
        String rawContent = post.getContent().trim();

        int linefeed = rawContent.indexOf('\n');
        if (-1 == linefeed) {
            title = rawContent;
            description = "";
        }
        else {
            title = rawContent.substring(0, linefeed);
            description = rawContent.substring(linefeed + 1);
        }

        return new RecommendPostDto(
                post.getImageUrls(),
                title,
                description,
                post.getId()
        );
    }

    public List<RecommendPostDto> getRecommendPosts() {
        return postRepository.findSome(5).stream()
                .map(this::makePostDto)
                .toList();
    }

    public Page<DestinationSummaryRes> getBannerDests(String bannerId, Pageable pageable) {
        return destinationService.convertToDto(
                bannerRepository.findAllDestinationsFromBanner(bannerId, pageable));
    }
}
