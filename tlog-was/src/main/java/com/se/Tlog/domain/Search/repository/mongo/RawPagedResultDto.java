package com.se.Tlog.domain.Search.repository.mongo;

import java.util.List;

import com.se.Tlog.domain.Travel.domain.Destination;

/**
 * MongoDB Atlas Search에서 사용할 페이징 결과 DTO입니다.
 * <br/> Spring MongoRepository는 검색과 페이징을 함께 지원하지 않습니다.
 * <br/> 따라서 Page 객체로의 변환을 위해 totalSize도 함께 전달받으려 이 DTO를 사용합니다. 
 */
record RawPagedResultDto(
        int totalSize,
        List<Destination> pagedDestinations) {

}
