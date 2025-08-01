package com.se.Tlog.domain.Tbti.controller.dto;

import com.se.Tlog.domain.Tbti.domain.Tbti;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "TBTI 특성에 관한 정보를 표시하는 데이터입니다.")
public record TbtiInfoRes(
        @Schema(description = "TBTI 유형입니다.", example = "RENA")
        String tbtiString,
        @Schema(description = "TBTI 유형의 다른 이름입니다.", example = "자연 속 즉흥 사색가")
        String secondName,
        @Schema(description = "TBTI 설명입니다.", example = "자연의 향기와 소리를 ~")
        String description,
        @Schema(description = "어울리는 TBTI 유형입니다.", example = "RENA")
        String preferredTbti,
        @Schema(description = "상극인 TBTI 유형입니다.", example = "RENA")
        String notPreferredTbti) {

    public static TbtiInfoRes getNullDto(Tbti tbti) {
        String tbtiStr = tbti.toString();
        return new TbtiInfoRes(
                tbtiStr,
                "아직 " + tbtiStr + "의 정보가 없어요...",
                "아직 " + tbtiStr + "가 어떤 성향인지 조사하고 있어요!",
                tbtiStr,
                tbtiStr);
    }
}
