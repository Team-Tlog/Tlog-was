package com.se.Tlog.domain.Tbti.domain;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TbtiTest {

    @Test
    @DisplayName("TBTI 값 객체 생성 테스트")
    public void testGenerate() {
        assertThatException().isThrownBy(() -> new Tbti(-1));
        assertThatNoException().isThrownBy(() -> new Tbti(0));
        assertThatNoException().isThrownBy(() -> new Tbti(50000));
        assertThatNoException().isThrownBy(() -> new Tbti(99999999));
        assertThatException().isThrownBy(() -> new Tbti(100000000));
    }

    @Test
    @DisplayName("TBTI 값 변환 테스트1")
    public void testConvert1() {
        Tbti value = new Tbti(12119931);
        assertEquals(12119931, value.getTbtiCode());
        assertEquals(12, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(11, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(99, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(31, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SENA", value.toString());
        
        value = new Tbti(120134);
        assertEquals(120134, value.getTbtiCode());
        assertEquals(0, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(12, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(1, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(34, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SELA", value.toString());

        value = new Tbti(1);
        assertEquals(1, value.getTbtiCode());
        assertEquals(0, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(0, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(0, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(1, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SELA", value.toString());

        value = new Tbti(99999999);
        assertEquals(99999999, value.getTbtiCode());
        assertEquals(99, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(99, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(99, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(99, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("RONI", value.toString());
    }

    @Test
    @DisplayName("TBTI 값 변환 테스트2")
    public void testConvert2() {
        Tbti value = new Tbti("SENA");
        assertEquals(9900, value.getTbtiCode());
        assertEquals(00, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(00, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(99, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(00, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SENA", value.toString());
        
        value = new Tbti("SELI");
        assertEquals(99, value.getTbtiCode());
        assertEquals(0, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(0, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(0, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(99, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SELI", value.toString());

        value = new Tbti("SOLI");
        assertEquals(990099, value.getTbtiCode());
        assertEquals(00, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(99, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(00, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(99, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SOLI", value.toString());

        value = new Tbti("RONI");
        assertEquals(99999999, value.getTbtiCode());
        assertEquals(99, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(99, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(99, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(99, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("RONI", value.toString());
    }
}
