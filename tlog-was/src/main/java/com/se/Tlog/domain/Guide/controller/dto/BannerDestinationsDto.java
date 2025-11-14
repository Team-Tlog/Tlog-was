package com.se.Tlog.domain.Guide.controller.dto;

import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class BannerDestinationsDto {
    private String title;
    private Page<DestinationSummaryRes> destinations;
}
