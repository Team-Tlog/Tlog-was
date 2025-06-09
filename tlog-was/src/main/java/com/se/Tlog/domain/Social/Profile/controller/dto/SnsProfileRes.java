package com.se.Tlog.domain.Social.Profile.controller.dto;

import org.springframework.data.domain.Page;

import com.se.Tlog.domain.Social.Post.controller.dto.PostPreviewRes;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SNS 프로필을 조회하는 데이터 형식입니다.")
public record SnsProfileRes(
        @Schema(description = "SNS ID값입니다.")
        String username, // SNS ID(대략 SNS에서의 닉네임)을 나타내는 값입니다. 사용자 기본 별명(nickname)이 아닙니다! 
        String profileImageUrl,
        String snsDescription,
        
        Long postCount,
        int followerCount,
        int followingCount,
        
        @Schema(description = "프로필에서 보여줄 게시글 미리보기의 첫 페이징입니다. (첫 갯수 24개) -> 추가 조회는 게시글 미리보기 api를 사용할 것!")
        Page<PostPreviewRes> posts) {

}
