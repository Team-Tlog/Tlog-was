package com.se.Tlog.domain.review;

import com.se.Tlog.domain.Review.application.ReviewService;
import com.se.Tlog.domain.Review.controller.dto.ReviewCreateDto;
import com.se.Tlog.domain.Travel.domain.Destination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = "spring.profiles.active=dev")
@AutoConfigureMockMvc
public class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService; // 실제 서비스


    @Autowired
    private MongoTemplate mongoTemplate;

    private final String TEST_DESTINATION_ID = "680f7ac05bf45e4afa8e59b5";

    @Test
    @DisplayName("동시성 테스트")
    void testConcurrentReviewCreation() throws InterruptedException {
        // 사전 검증: Destination이 실제로 존재하는지 체크
        Destination existingDestination = mongoTemplate.findById(TEST_DESTINATION_ID, Destination.class);
        assertThat(existingDestination).as("테스트할 Destination이 DB에 존재해야 합니다.").isNotNull();

        int numberOfThreads = 20; // 동시에 요청할 쓰레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        Random random = new Random();
        for (int i = 0; i < numberOfThreads; i++) {
            int threadNumber = i;
            executorService.submit(() -> {
                try {
                    int randomRating = random.nextInt(5) + 1;
                    ReviewCreateDto reviewDto = new ReviewCreateDto(
                            "54aa01c5-b5ff-4153-a766-3ddc3af2fa58",           // userId
                            TEST_DESTINATION_ID,             // destinationId
                            "테스트 사용자 " + threadNumber,    // username
                            randomRating,                               // rating
                            "테스트 리뷰 내용 " + threadNumber,  // content
                            List.of(),                       // imageUrlList (empty)
                            List.of("하하", "히히", "쿄쿄")                        // customTagNames (empty)
                    );
                    reviewService.createReview(reviewDto);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기
        Thread.sleep(1000);
        // 최종적으로 Destination의 reviewCount가 예상값과 일치하는지 확인
        Destination updatedDestination = mongoTemplate.findById(TEST_DESTINATION_ID, Destination.class);

        assertThat(updatedDestination.getReviewCount()).isGreaterThanOrEqualTo(numberOfThreads);
    }

}
