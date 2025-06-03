package com.se.Tlog.domain.Tbti.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

public enum TraitCategory {
    RISK_TAKING("S-R"), // 교류형(S) ↔ 사색형(R) 0 ~ 99
    LOCATION_PREFERENCE("E-O"), // 자연친화(E) ↔ 도시친화(O) 0 ~ 99
    PLANNING_STYLE("L-N"), // 계획적(L) ↔ 즉흥적(N) 0 ~ 99
    ACTIVITY_LEVEL("A-I"); // 활동형(A) ↔ 여유형(I) 0 ~ 99

    private String categoryInitial;
    private TraitCategory(String categoryInitial) {
        this.categoryInitial = categoryInitial;
    }
    public String getCategoryInitial() {
        return categoryInitial;
    }
    
    public static TraitCategory fromString(String traitCategory) {
        try {
            return TraitCategory.valueOf(traitCategory.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException(ErrorType.INVALID_TBTI_CATEGORY);
        }
    }
}
