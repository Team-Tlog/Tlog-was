package com.se.Tlog.domain.Search.controller.dto;

import com.se.Tlog.domain.Travel.domain.Destination;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "최근 인기있는 여행지 데이터입니다.")
public record PopularDestinationDto(
        @Schema(example = "전국/서울 등등 지역이름")
        String region,
        @Schema(example = "https://example.com/image")
        String imageUrl,
        @Schema(example = "11223344aabbccdd5566eeff")
        String destinationId
) {
        public static PopularDestinationDto create(Destination destination) {
                return create(destination, destination.getCity());
        }

        public static PopularDestinationDto create(Destination destination, String customRegionName) {
                return new PopularDestinationDto(customRegionName, destination.getImageUrl(), destination.getId());
        }
}
