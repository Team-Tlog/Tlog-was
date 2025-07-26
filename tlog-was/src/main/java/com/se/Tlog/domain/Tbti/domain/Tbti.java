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
    
    private int rawTbtiCode;
    
    public Tbti(int tbtiCode) { 
        if (tbtiCode < MIN_TBTI_CODE || tbtiCode > MAX_TBTI_CODE)
            throw new CustomException(ErrorType.INVALID_TBTI_CODE);
        this.rawTbtiCode = tbtiCode;
    }
    
    public Tbti(String tbtiString) {
        TraitCategory[] categories = TraitCategory.values();
        if (tbtiString == null || tbtiString.length() != categories.length)
            throw new CustomException(ErrorType.INVALID_TBTI_STRING);
        
        tbtiString = tbtiString.toUpperCase();
        rawTbtiCode = 0;
        for (int i = 0; i < categories.length; i++) {
            rawTbtiCode *= 100;
            rawTbtiCode += categories[i].getPercentage(tbtiString.charAt(i));
        }
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
    
    public static int getSameFeatureCount(int code1, int code2) {
        int cnt = 0;
        for (int m = 1; m <= 1_00_00_00; m *= 100)
            if (((code1 / m % 100) < FEATURE_THRESHOLD)
                == ((code2 / m % 100) < FEATURE_THRESHOLD))
                cnt++;
        return cnt;
    }
}
