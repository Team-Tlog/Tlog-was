package com.se.Tlog.domain.Guide.repository.mongo;

import com.se.Tlog.domain.Guide.domain.RecDest;
import com.se.Tlog.domain.Guide.repository.dto.FullRecDest;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecDestRepository extends MongoRepository<RecDest, String> {

    // 1. 무작위 하나의 항목만 검색합니다.
    static final String QUERY_1_SAMPLE = "{ '$sample': { 'size': ?0 } }";

    // 2. 문자열 형태인 여행지ID를 ObjectId 타입으로 변환합니다.
    static final String QUERY_2_CONVERT_ID = """
            {
              "$addFields": {
                "destIds": {
                  "$map": {
                    "input": "$destinationIds",
                    "as": "destId",
                    "in": {
                      "$convert": {
                        "input": "$$destId",
                        "to": "objectId"
                      }
                    }
                  }
                }
              }
            }
            """;

    // 3. 여행지 데이터를 Join해 가져옵니다.
    static final String QUERY_3_JOIN_WITH_DESTINATION = """
            {
              "$lookup": {
                "from": "destinations",
                "localField": "destIds",
                "foreignField": "_id",
                "pipeline": [
                  {
                    "$project": {
                      "_id": 1,
                      "imageUrl": 1,
                      "name": 1
                    }
                  }
                ],
                "as": "destinations"
              }
            }
            """;

    @Aggregation(pipeline = {
            QUERY_1_SAMPLE,
            QUERY_2_CONVERT_ID,
            QUERY_3_JOIN_WITH_DESTINATION
    })
    List<FullRecDest> findSomeWithDestinations(int size);
}
