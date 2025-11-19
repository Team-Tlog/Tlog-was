package com.se.Tlog.domain.Search.batch;

import com.se.Tlog.domain.Search.domain.DestinationPopularity;
import com.se.Tlog.domain.Search.repository.mongo.PopularityRepository;
import com.se.Tlog.domain.Search.repository.redis.PopularityCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PopularitySyncJob {
    private final PopularityCacheRepository popularityCacheRepository;
    private final PopularityRepository popularityRepository;

    @Scheduled(fixedRate = 2 * 60 * 1000) // 2분마다 갱신
    public void syncPopularityToMongo() {
        Map<String, Long> destinationScores = popularityCacheRepository.popAllCachedScore();
        int count = destinationScores.size();
        if (count == 0)
            return;

        List<DestinationPopularity> popularities = popularityRepository.findAllById(destinationScores.keySet());
        popularities.forEach(pop -> {
            pop.updateScore(destinationScores.get(pop.getDestinationId()));
            destinationScores.remove(pop.getDestinationId());
        });
        destinationScores.keySet().forEach(id -> {
            popularities.add(DestinationPopularity.create(id, destinationScores.get(id)));
        });

        log.info("Syncing destination's popularity scores (dest cnt : {})", count);
        popularityRepository.saveAll(popularities);
    }
}
