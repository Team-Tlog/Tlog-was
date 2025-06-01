package com.se.Tlog.domain.Course.repository.dto;

import java.util.List;

/**
 * 코스가 거쳐가는 모든 지역구를 반환하는 데이터 형식입니다.
 */
public record CourseDistrictsDto(
        String courseId,
        List<String> districts) {

}
