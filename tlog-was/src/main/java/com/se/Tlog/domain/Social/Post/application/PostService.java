package com.se.Tlog.domain.Social.Post.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Course.domain.Course;
import com.se.Tlog.domain.Course.repository.dto.CourseDistrictsDto;
import com.se.Tlog.domain.Course.repository.mongo.CourseRepository;
import com.se.Tlog.domain.Social.Follow.repository.jpa.FollowRepository;
import com.se.Tlog.domain.Social.Post.controller.dto.CreatePostReq;
import com.se.Tlog.domain.Social.Post.controller.dto.PostDetailRes;
import com.se.Tlog.domain.Social.Post.controller.dto.PostPreviewRes;
import com.se.Tlog.domain.Social.Post.controller.dto.ReplyRes;
import com.se.Tlog.domain.Social.Post.domain.Post;
import com.se.Tlog.domain.Social.Post.repository.mongo.PostQueryRepository;
import com.se.Tlog.domain.Social.Post.repository.mongo.PostRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@ApplicationService
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final FollowRepository followRepository;
    private final ReplyService replyService;
    
    // 게시물 첫 조회시 기본으로 전송할 댓글 수 입니다.
    private static final int DEFAULT_REPLY_COUNT = 10;
    
    public String createPost(CreatePostReq request) {
        if (!userRepository.existsById(request.author()))
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        if (!courseRepository.existsById(request.courseId()))
            throw new CustomException(ErrorType.COURSE_NOT_FOUND);
        if (request.content() == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        
        return postRepository.save(
                Post.create(request.author(), request.courseId(), request.content(), request.imageUrls())
                ).getId();
    }
    
    public Page<PostPreviewRes> getPreviewUserPosts(UUID userId, Pageable pageable) {
        if (!userRepository.existsById(userId))
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        
        return postRepository.findAllPreviewByAuthor(userId, pageable)
                .map(post -> {
                    if (0 < post.getImageUrls().size())
                        return new PostPreviewRes(post.getId(), post.getImageUrls().get(0));
                    else
                        return new PostPreviewRes(post.getId(), "");
                });
    }
    
    public PostDetailRes getPostDetail(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorType.POST_NOT_FOUND));
        User author = userRepository.findById(post.getAuthor())
                .orElseThrow(() -> {
                    log.error("SNS 게시글 조회 오류 : 탈퇴한 작성자의 게시글을 조회하려 하였습니다. (post id : " + post.getId() + ")");
                    return new CustomException(ErrorType.POST_NOT_FOUND);
                });
        Course course = courseRepository.findById(post.getCourseId())
                .orElseThrow(() -> {
                    log.error("SNS 게시글 조회 오류 : 삭제된 코스가 참조된 게시글을 조회하려 하였습니다. (post id : " + post.getId() + ")");
                    return new CustomException(ErrorType.POST_NOT_FOUND);
                });
        CourseDistrictsDto courseDestinations = courseRepository.findAllDestinationNameInCourse(course.getId());
        List<ReplyRes> replies = replyService.getReplyResOfPost(DEFAULT_REPLY_COUNT, null, postId);
        
        return new PostDetailRes(
                post.getId(), 
                post.getLikeCount(),
                postId,
                course.getId(), 
                courseDestinations.districts(), 
                author.getId(),
                author.getSnsId(),
                author.getProfileImage(),
                post.getImageUrls(),
                post.getContent(),
                replies);
    }
    
    public Slice<PostDetailRes> getRecentFollowersPosts(int size, String lastPostId, UUID userId) {
        if (size <= 0) size = 10;
        
        List<UUID> followings = followRepository.findToUserIdsByFromUserId(
                userId, 
                PageRequest.of(0, Integer.MAX_VALUE)
                ).toList();
        Slice<Post> posts = postQueryRepository.findAllRecentPosts(size, lastPostId, followings);
        Map<UUID, User> authors = userRepository.findAllById(posts.map(post -> post.getAuthor()).toSet())
                .stream().collect(Collectors.toMap(
                        user -> user.getId(),
                        user -> user));
        Map<String, CourseDistrictsDto> courseDestinations = courseRepository.findAllDestinationNameInCourses(
                posts.map(post -> new ObjectId(post.getCourseId())).toSet()
                ).stream().collect(Collectors.toMap(
                        dto -> dto.courseId(),
                        dto -> dto));
        Map<String, List<ReplyRes>> replies = replyService.getReplyResOfPosts(
                size, 
                posts.map(post->post.getId()).toSet());
        
        return posts.map(post -> {
            CourseDistrictsDto courseDestination = courseDestinations.get(post.getCourseId());
            User author = authors.get(post.getAuthor());
            List<ReplyRes> postReplies = replies.get(post.getId());
            return new PostDetailRes(
                    post.getId(), 
                    post.getLikeCount(),
                    post.getId(),
                    post.getCourseId(), 
                    null != courseDestination ? courseDestination.districts() : new ArrayList<String>(), 
                    post.getAuthor(),
                    null != author ? author.getSnsId() : "탈퇴한 사용자입니다.",
                    null != author ? author.getProfileImage() : "",
                    post.getImageUrls(),
                    post.getContent(),
                    null != postReplies ? postReplies : new ArrayList<ReplyRes>());
        });
    }
}
