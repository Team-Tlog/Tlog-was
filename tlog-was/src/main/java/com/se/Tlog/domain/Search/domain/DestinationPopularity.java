package com.se.Tlog.domain.Search.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "destination_popularity")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DestinationPopularity {
    @Id
    private String destinationId;
    private long score;

    public void updateScore(long delta) {
        score += delta;
    }

    public static DestinationPopularity create(String destinationId, long score) {
        if (destinationId == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        return new DestinationPopularity(destinationId, score);
    }
}
