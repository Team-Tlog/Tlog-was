package com.se.Tlog.domain.Search.repository.api;

import com.se.Tlog.domain.Search.repository.dto.AiDestinationReq;
import com.se.Tlog.domain.Search.repository.dto.AiDestinationRes;
import com.se.Tlog.domain.User.repository.jpa.UserTagRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiRecApi {
    @Value("${external.url.tlog-ai}")
    private String TLOG_AI_URL;
    private final UserTagRepository userTagRepository;

    public List<String> getDestinationIds(UUID userId, int regionCode, int size) {
        return getDestination(userId, List.of(regionCode), size).destinations()
                .stream().map(AiDestinationRes.AiDestination::id)
                .toList();
    }

    public AiDestinationRes getDestination(UUID userId, List<Integer> regionCodes, int size) {
        // 태그를 DB측에 순서대로 저장해두었기 때문에, 가중치 순서는 AI API 호출 순서와 동일하게 받아옵니다.
        List<Double> tagWeights = userTagRepository.findWeightsByUserId(userId);
        return getDestination(new AiDestinationReq(tagWeights, regionCodes, size));
    }

    public AiDestinationRes getDestination(AiDestinationReq request) {
        return RestClient.create(TLOG_AI_URL).post()
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("AI 추천 요청에 실패했습니다.");
                    log.error("request : " + req.getURI().toString() + req.getHeaders().toString());
                    log.error("response : " + new String(res.getBody().readAllBytes()));
                    throw new CustomException(ErrorType.AI_DESTINATION_FETCH_FAIL);
                })
                .body(AiDestinationRes.class);
    }
}
