package com.se.Tlog.domain.Guide.repository.mongo;

import com.se.Tlog.domain.Guide.domain.RecBanner;
import com.se.Tlog.domain.Travel.domain.Destination;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecBannerRepository {
    private final MongoTemplate mongoTemplate;
    private final String COLLECTION_NAME = "recommend_banner";

    public List<RecBanner> findAll() {
        return mongoTemplate.findAll(RecBanner.class, COLLECTION_NAME);
    }

    @Data
    private class DestResults {
        List<Destination> destinations;
        int count;
    }

    public Page<Destination> findAllDestinationsFromBanner(String bannerId, Pageable pageable) {
        Aggregation query = Aggregation.newAggregation(
                // 1. id로 배너를 검색합니다.
                Aggregation.match(Criteria.where("_id").is(new ObjectId(bannerId))),
                // 2. 문자열 형태인 여행지ID를 ObjectId 타입으로 변환합니다.
                Aggregation.addFields()
                        .addField("destIds").withValue(
                                VariableOperators.Map
                                        .itemsOf("destinationIds")
                                        .as("destId")
                                        .andApply(
                                                ConvertOperators.Convert
                                                        .convertValue("$$destId")
                                                        .to(JsonSchemaObject.Type.objectIdType())))
                        .build(),
                // 3. 여행지 데이터를 Join해 가져옵니다.
                Aggregation.lookup()
                        .from("destinations")
                        .localField("destIds")
                        .foreignField("_id")
                        .as("destinations"),
                // 4. 페이징 처리합니다.
                Aggregation.project()
                        .and("destinations").size().as("count")
                        .and("destinations").slice(pageable.getPageSize(), (int)pageable.getOffset()).as("destinations")
        );

        DestResults result = mongoTemplate.aggregate(query, COLLECTION_NAME, DestResults.class)
                .getUniqueMappedResult();
        return new PageImpl<>(
                result.getDestinations(),
                pageable,
                result.getCount());
    }
}
