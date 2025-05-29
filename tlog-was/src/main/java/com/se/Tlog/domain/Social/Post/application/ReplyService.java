package com.se.Tlog.domain.Social.Post.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Social.Post.controller.dto.ReplyRes;
import com.se.Tlog.domain.Social.Post.domain.Reply;
import com.se.Tlog.domain.Social.Post.repository.mongo.PostRepository;
import com.se.Tlog.domain.Social.Post.repository.mongo.ReplyRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@ApplicationService
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public String attachReplyToPost(String postId, UUID authorId, String content) {
        if (!userRepository.existsById(authorId))
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        if (!postRepository.existsById(postId))
            throw new CustomException(ErrorType.POST_NOT_FOUND);
        if (content == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        
        return replyRepository.save(Reply.create(postId, null, authorId, content))
                .getId();
    }
    
    public String attachReplyToReply(String replyId, UUID authorId, String content) {
        if (!userRepository.existsById(authorId))
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        Reply parentReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ErrorType.REPLY_NOT_FOUND));
        if (content == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        
        parentReply.addNestedReply();
        replyRepository.save(parentReply);
        return replyRepository.save(Reply.create(parentReply.getPostId(), replyId, authorId, content))
                .getId();
    }
    
    public ReplyRes getReply(String replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ErrorType.REPLY_NOT_FOUND));
        User author = userRepository.findById(reply.getAuthorId())
                .orElseThrow(() -> {
                    log.error("댓글 조회 오류 : 탈퇴한 작성자의 댓글을 조회하려 시도했습니다. (reply id : " + replyId + ")");
                    return new CustomException(ErrorType.REPLY_NOT_FOUND); 
                });
        
        return new ReplyRes(
                reply.getId(),
                reply.getContent(),
                reply.getNestedReplyCount(),
                author.getId(),
                author.getName(),
                author.getProfileImage());
    }

    public List<ReplyRes> getReplyResOfPost(int size, String lastReplyId, String postId) {
        if (!postRepository.existsById(postId))
            throw new CustomException(ErrorType.POST_NOT_FOUND);
        if (size <= 0 || (null != lastReplyId && !ObjectId.isValid(lastReplyId)))
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        
        List<Reply> replyList = replyRepository.findAllOfPost(size, lastReplyId, postId);
        Map<UUID, User> users = userRepository.findAllById(
                replyList.stream().map(r -> r.getAuthorId()).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(
                        u -> u.getId(),
                        u -> u));
        if (users.size() != replyList.size())
            log.error("댓글 조회 오류 : 게시글 내 댓글 중 탈퇴한 작성자의 댓글이 남아있습니다. (post id : " + postId + ")");
        
        return replyList.stream()
            .filter(reply -> users.containsKey(reply.getAuthorId()))
            .map(reply -> {
                User author = users.get(reply.getAuthorId());
                return new ReplyRes(
                        reply.getId(),
                        reply.getContent(),
                        reply.getNestedReplyCount(),
                        author.getId(),
                        author.getName(),
                        author.getProfileImage());
            }).toList();
    }
    
    public List<ReplyRes> getReplyResOfReply(int size, String lastReplyId, String replyId) {
        if (!replyRepository.existsById(replyId))
            throw new CustomException(ErrorType.REPLY_NOT_FOUND);
        if (size <= 0 || (null != lastReplyId && !ObjectId.isValid(lastReplyId)))
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        
        List<Reply> replyList = replyRepository.findAllOfReply(size, lastReplyId, replyId);
        Map<UUID, User> users = userRepository.findAllById(
                replyList.stream().map(r -> r.getAuthorId()).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(
                        u -> u.getId(),
                        u -> u));
        if (users.size() != replyList.size())
            log.error("댓글 조회 오류 : 대댓글 중 탈퇴한 작성자의 댓글이 남아있습니다. (reply id : " + replyId + ")");
        
        return replyList.stream()
            .filter(reply -> users.containsKey(reply.getAuthorId()))
            .map(reply -> {
                User author = users.get(reply.getAuthorId());
                return new ReplyRes(
                        reply.getId(),
                        reply.getContent(),
                        reply.getNestedReplyCount(),
                        author.getId(),
                        author.getName(),
                        author.getProfileImage());
            }).toList();
    }
}
