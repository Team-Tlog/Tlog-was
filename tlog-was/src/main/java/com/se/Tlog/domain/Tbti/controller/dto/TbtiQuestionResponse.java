package com.se.Tlog.domain.Tbti.controller.dto;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "응답된 TBTI 질문을 담는 Page 객체입니다.")
public abstract class TbtiQuestionResponse implements Page<TbtiQuestionRes> {
	
}
