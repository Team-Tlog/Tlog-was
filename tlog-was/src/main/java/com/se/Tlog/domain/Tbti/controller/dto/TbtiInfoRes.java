package com.se.Tlog.domain.Tbti.controller.dto;

import com.se.Tlog.domain.Tbti.domain.Tbti;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "TBTI 특성에 관한 정보를 표시하는 데이터입니다.")
public record TbtiInfoRes(
        String tbtiString,
        String secondName,
        String description,
        String preferredTbti,
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
