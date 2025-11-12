package com.se.Tlog.domain.User.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTagRes {
    @Schema(example = "바다")
    private String name;
    @Schema(example = "0.65")
    private double weight;
}
