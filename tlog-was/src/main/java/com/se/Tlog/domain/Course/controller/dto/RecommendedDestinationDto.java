package com.se.Tlog.domain.Course.controller.dto;

import com.se.Tlog.domain.Search.repository.dto.AiDestinationRes;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.Location;
import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistDestinationRes;
import lombok.Builder;

import java.util.List;


@Builder
public record RecommendedDestinationDto(
        String id,
        String name,
        String description,
        String city,
        String district,
        Location location,
        String imageUrl,
        List<String> tags,
        List<TagCount> tagCountList,
        Double similarityScore,
        boolean isFromWishlist
) {

    public static RecommendedDestinationDto fromAiDestination(AiDestinationRes.AiDestination aiDest,
                                                              List<String> tagNames) {
        return RecommendedDestinationDto.builder()
                .id(aiDest.id())
                .name(aiDest.name())
                .description(aiDest.description())
                .city(aiDest.city())
                .district(aiDest.district())
                .tags(tagNames)
                .similarityScore(aiDest.similarity_score())
                .isFromWishlist(false)
                .build();
    }


    public static RecommendedDestinationDto fromWishlistDestination(WishlistDestinationRes wishlistDest) {
        return RecommendedDestinationDto.builder()
                .id(wishlistDest.id())
                .name(wishlistDest.name())
                .description(wishlistDest.description())
                .city(wishlistDest.city())
                .district(wishlistDest.district())
                .location(wishlistDest.location())
                .imageUrl(wishlistDest.imageUrl())
                .tagCountList(wishlistDest.tagCountList())
                .isFromWishlist(true)
                .build();
    }

    public static RecommendedDestinationDto fromDestinationDetail(
            Destination dest,
            List<TagCount> tagCountList) {
        return RecommendedDestinationDto.builder()
                .id(dest.getId())
                .name(dest.getName())
                .description(dest.getDescription())
                .city(dest.getCity())
                .district(dest.getDistrict())
                .imageUrl(dest.getImageUrl())
                .tagCountList(tagCountList)
                .isFromWishlist(false)
                .build();
    }
}
