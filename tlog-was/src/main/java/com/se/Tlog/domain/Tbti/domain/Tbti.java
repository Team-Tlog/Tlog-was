package com.se.Tlog.domain.Tbti.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

/**
 * TBTI Value Object
 */
public class Tbti {
    public static final int MIN_TBTI_CODE = 00000000;
    public static final int MAX_TBTI_CODE = 99999999;
    public static final int FEATURE_THRESHOLD = 50;

    public static int toTbtiCode(String tbtiCode) {
        try {
            if (tbtiCode.length() != 8)
                throw new CustomException(ErrorType.INVALID_TBTI_CODE);
            return Integer.parseInt(tbtiCode);
        } catch (Exception e) {
            throw new CustomException(ErrorType.INVALID_TBTI_CODE);
        }
    }
    
    private int rawTbtiCode;
    
    public Tbti(int tbtiCode) { 
        if (tbtiCode < MIN_TBTI_CODE || tbtiCode > MAX_TBTI_CODE)
            throw new CustomException(ErrorType.INVALID_TBTI_CODE);
        this.rawTbtiCode = tbtiCode;
    }
    
    public Tbti(String tbtiCode) {
        this(toTbtiCode(tbtiCode));
    }
    
    public int getTbtiCode() {
        return rawTbtiCode;
    }
    
    public int getPercentage(TraitCategory category) {
        return switch (category) {
            case RISK_TAKING -> (rawTbtiCode / 1000000);
            case LOCATION_PREFERENCE -> (rawTbtiCode / 10000) % 100;
            case PLANNING_STYLE -> (rawTbtiCode / 100) % 100;
            case ACTIVITY_LEVEL -> rawTbtiCode % 100;
        };
    }
    
    public String toString() {
        StringBuilder tbtiStr = new StringBuilder();
        for (TraitCategory c : TraitCategory.values())            
            tbtiStr.append((getPercentage(c) < FEATURE_THRESHOLD
                    ? c.getMinCategoryInitial()
                    : c.getMaxCategoryInitial()));
        return tbtiStr.toString();
    }
}
