package com.se.Tlog.domain.Course.controller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "여행 코스 계획의 한 일자를 나타냅니다.")
public record CourseDateDto(
        // @Schema(description = "날짜의 번호를 나타냅니다. (n일차)")
        // int day,
        
        @Schema(description = "이 날짜의 여행지. 특정 기준(지역별 등)으로 그룹화되어 있습니다.")
        List<DestinationGroupDto> destinationGroups
        ) {

    public static CourseDateDto groupByCity(List<DestinationSummaryRes> rawDestinations) {
        Map<String, DestinationGroupDto> groupDtoMap = new HashMap<String, DestinationGroupDto>();
        
        for (DestinationSummaryRes d : rawDestinations) {
            String cityName = d.city();
            if (!groupDtoMap.containsKey(cityName))
                groupDtoMap.put(cityName, new DestinationGroupDto(cityName, new ArrayList<DestinationSummaryRes>()));
            groupDtoMap.get(cityName).destinations().add(d);
        }
        
        return new CourseDateDto(new ArrayList<DestinationGroupDto>(groupDtoMap.values()));
    }
}
