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

        assertThatException().isThrownBy(() -> new Tbti(null));
        assertThatException().isThrownBy(() -> new Tbti(""));
        assertThatException().isThrownBy(() -> new Tbti("hello"));
        assertThatException().isThrownBy(() -> new Tbti("1.15"));
        assertThatException().isThrownBy(() -> new Tbti("-1"));
        assertThatException().isThrownBy(() -> new Tbti("15123"));
        assertThatException().isThrownBy(() -> new Tbti("001200"));
        assertThatException().isThrownBy(() -> new Tbti("15123--1"));
        assertThatException().isThrownBy(() -> new Tbti("1.121231"));
        assertThatException().isThrownBy(() -> new Tbti("0ii01200"));
        assertThatException().isThrownBy(() -> new Tbti("-1121231"));
        
        assertThatNoException().isThrownBy(() -> new Tbti("00000000"));
        assertThatNoException().isThrownBy(() -> new Tbti("99999999"));
        assertThatNoException().isThrownBy(() -> new Tbti("12345678"));
        assertThatNoException().isThrownBy(() -> new Tbti("10000010"));
    }

    @Test
    @DisplayName("TBTI 값 변환 테스트")
    public void testConvert() {
        Tbti value = new Tbti(12119931);
        assertEquals(12, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(11, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(99, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(31, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SENA", value.toString());
        
        value = new Tbti("00120134");
        assertEquals(00, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(12, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(01, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(34, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SELA", value.toString());

        value = new Tbti("00000001");
        assertEquals(00, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(00, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(00, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(01, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("SELA", value.toString());

        value = new Tbti(99999999);
        assertEquals(99, value.getPercentage(TraitCategory.RISK_TAKING));
        assertEquals(99, value.getPercentage(TraitCategory.LOCATION_PREFERENCE));
        assertEquals(99, value.getPercentage(TraitCategory.PLANNING_STYLE));
        assertEquals(99, value.getPercentage(TraitCategory.ACTIVITY_LEVEL));
        assertEquals("RONI", value.toString());
    }
}
