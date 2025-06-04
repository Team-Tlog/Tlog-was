package com.se.Tlog.domain.Tbti.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "TBTI 설명 데이터 추가시 필요한 정보입니다.")
public record CreateTbtiInfoReq(
        @Schema(description = "TBTI 유형입니다.", example = "RENA")
        String tbtiString,
        
        @Schema(description = "TBTI 캐릭터 일러스트입니다.")
        String imageUrl,
        
        @Schema(description = "TBTI 유형의 다른 이름입니다.", example = "자연 속 즉흥 사색가")
        String secondName,
        
        @Schema(description = "TBTI 설명입니다.", example = "자연의 향기와 소리를 ~")
        String description
        ) {

}
