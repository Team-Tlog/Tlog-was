package com.se.Tlog.domain.Guide.controller.dto;

import com.se.Tlog.domain.Guide.domain.RecBanner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BannerDto {
    private String id;
    private String imageUrl;
    private String title;
    @Schema(example = "[\"해시\",\"태그\",\"샵(#)은\",\"없습니다\"]")
    private List<String> hashtags;

    /*
     * 배너 데이터로는 아직 사용하지 않습니다.
    @Schema(example = "(관련있는 경우 제공됩니다) https://related.url/")
    private String relatedUrl;
    @Schema(example = "(관련있는 경우 제공됩니다) 여행지 id")
    private List<String> destinationIds;
    */

    public static BannerDto create(RecBanner banner) {
        return create(banner, banner.getTitle());
    }

    public static BannerDto create(RecBanner banner, String title) {
        return new BannerDto(
                banner.getId(),
                banner.getImageUrl(),
                title,
                banner.getHashTags()
        );
    }
}
