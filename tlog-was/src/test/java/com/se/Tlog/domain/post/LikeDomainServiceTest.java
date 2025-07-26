package com.se.Tlog.domain.post;

import com.se.Tlog.domain.Social.Post.like.infra.LikeCacheRepository;
import com.se.Tlog.domain.Social.Post.like.service.LikeDomainService;
import com.se.Tlog.domain.Social.Post.like.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeDomainServiceTest {

    @Mock
    private LikeCacheRepository likeCacheRepository;

    @InjectMocks
    private LikeDomainService likeDomainService;

    private LikeService likeService;

    @Nested
    @DisplayName("toggle() - 좋아요 API")
    class LikeToUser{
        private final String postId = "post-123";
        private final UUID userId = UUID.randomUUID();

        @Test
        @DisplayName("toggle() - 아직 좋아요를 누르지 않은 경우, 좋아요를 추가한다")
        void addLikeIfNotAlreadyLiked() {
            // given
            when(likeCacheRepository.isAlreadyLiked(postId, userId)).thenReturn(false);

            likeService = new LikeService(likeDomainService);

            //when
            assertThatCode(() -> likeService.toggle(postId, userId))
                    .doesNotThrowAnyException();

            //then
            verify(likeCacheRepository).addLike(postId, userId); // 좋아요를 눌렀다는 컷을 캐시에 저장했는지 확인
            verify(likeCacheRepository).incrementLikeCount(postId); // 좋아요 수를 1 증가시켰는지 확인
            verify(likeCacheRepository, never()).removeLike(any(), any()); // 좋아요를 삭제하지 않았는지 검증
            verify(likeCacheRepository, never()).decrementLikeCount(any()); // 좋아요 수를 감소시키지 않았는지 검증
        }

        @Test
        @DisplayName("toggle() - 이미 좋아요를 누른 경우, 좋아요를 취소한다")
        void removeLikeIfAlreadyLiked() {
            // given
            when(likeCacheRepository.isAlreadyLiked(postId, userId)).thenReturn(true);

            likeService = new LikeService(likeDomainService);

            // when
            assertThatCode(() -> likeService.toggle(postId, userId))
                    .doesNotThrowAnyException();

            // then
            verify(likeCacheRepository).removeLike(postId, userId);
            verify(likeCacheRepository).decrementLikeCount(postId);
            verify(likeCacheRepository, never()).addLike(any(), any());
            verify(likeCacheRepository, never()).incrementLikeCount(any());
        }
    }
}
