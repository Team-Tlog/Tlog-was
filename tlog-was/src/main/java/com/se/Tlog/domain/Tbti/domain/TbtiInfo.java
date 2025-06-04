package com.se.Tlog.domain.Tbti.domain;

import static lombok.AccessLevel.PROTECTED;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TBTI 관련 각종 정보(일러스트, 설명, 한줄설명 등)를 관리하는 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TbtiInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String tbtiString;
    
    private String imageUrl;
    
    private String secondName;
    
    private String description;
    
    private TbtiInfo(String tbtiString, String imageUrl, String secondName, String description) {
        this.tbtiString = tbtiString;
        this.imageUrl = imageUrl;
        this.secondName = secondName;
        this.description = description;
    }
    
    public static TbtiInfo create(Tbti tbti, String imageUrl, String secondName, String description) {
        if (tbti == null
                || imageUrl == null
                || secondName == null
                || description == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        
        return new TbtiInfo(tbti.toString(), imageUrl, secondName, description);
    }
}
