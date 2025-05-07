package com.se.Tlog.domain.Travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import com.se.Tlog.domain.Travel.domain.Location;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "여행지 생성 요청 DTO")
public class DestinationDto {

    @Schema(description = "여행지 이름입니다.")
    private String name;
    @Schema(description = "여행지 주소입니다.")
    private String address;
    @Schema(description = "여행지 위치 입니다.")
    private Location location;
    @Schema(description = "여행지의 구체적인 위치 시입니다.")
    private String city;
    @Schema(description = "여행지 구체적인 구 입니다.")
    private String district;
    @Schema(description = "여행지 주차가 가능 여부입니다.")
    private boolean hasParking;
    @Schema(description = "여행지 애완동물 동반 가능 여부입니다.")
    private boolean petFriendly;
    @Schema(description = "여행지 이미지 url입니다.")
    private String imageUrl;
    @Schema(description = "여행지 부가적인 설명입니다.")
    private String description;
    @Schema(description = "여행지 커스텀 태그입니다.")
    private List<String> customTags;
    
}