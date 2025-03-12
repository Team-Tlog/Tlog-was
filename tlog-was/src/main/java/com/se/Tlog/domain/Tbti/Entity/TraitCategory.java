package com.se.Tlog.domain.Tbti.Entity;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

public enum TraitCategory {
    ACTIVITY_LEVEL, // 활동적 ↔ 여유로움
    LOCATION_PREFERENCE, // 자연 ↔ 도시
    PLANNING_STYLE, // 계획적 ↔ 즉흥적
    RISK_TAKING; // 개척적 ↔ 안정적

    public static TraitCategory fromString(String traitCategory) {
        try {
            return TraitCategory.valueOf(traitCategory.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(ErrorType.INVALID_TBTI_CATEGORY);
        }
    }
}
