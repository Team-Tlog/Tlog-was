package com.se.Tlog.domain.Social.Post.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.Getter;

@Document(collection = "sns_reply")
@Getter
public class Reply {
    private static final String NULL_PARENT_ID = "000000000000000000000000";
    
    @Id
    private String id;
    private String postId;
    private String parentId;
    
    private UUID authorId;
    private String content;
    private int nestedReplyCount;
    
    public void addNestedReply() {
        nestedReplyCount++;
    }
    
    public void removeNestedReply() {
        if (nestedReplyCount > 0)
            nestedReplyCount--;
    }
    
    private Reply(String postId, String parentId, UUID authorId, String content) {
        this.postId = postId;
        this.parentId = parentId;
        this.authorId = authorId;
        this.content = content;
        this.nestedReplyCount = 0;
    }
    
    public static Reply create(String postId, String parentId, UUID authorId, String content) {
        if (postId == null || authorId == null || content == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        if (parentId == null)
            parentId = NULL_PARENT_ID;
        return new Reply(postId, parentId, authorId, content);
    }
}
