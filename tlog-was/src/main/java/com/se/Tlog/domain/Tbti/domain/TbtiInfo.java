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
    
    private String secondName;
    
    private String description;

    private String preferred;

    private String notPreferred;
    
    private TbtiInfo(String tbtiString, String secondName, String description, String preferred, String notPreferred) {
        this.tbtiString = tbtiString;
        this.secondName = secondName;
        this.description = description;
        this.preferred = preferred;
        this.notPreferred = notPreferred;
    }
    
    public static TbtiInfo create(Tbti tbti, String secondName, String description, Tbti preferred, Tbti notPreferred) {
        if (tbti == null
                || secondName == null
                || description == null
                || preferred == null
                || notPreferred == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        
        return new TbtiInfo(tbti.toString(), secondName, description, preferred.toString(), notPreferred.toString());
    }
}
