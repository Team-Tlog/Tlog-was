package com.se.Tlog.domain.Social.Post.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.Getter;

@Document(collection = "sns_post")
@Getter
public class Post {
    @Id
    private String id;
    
    private UUID author;
    
    private String courseId;
    
    private String content;
    
    private List<String> imageUrls;
    
    private int likeCount;
    
    public void updateLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    private Post(UUID author, String courseId, String content, List<String> imageUrls) {
        this.author = author;
        this.courseId = courseId;
        this.content = content;
        this.imageUrls = new ArrayList<String>(imageUrls);
        this.likeCount = 0;
    }
    
    public static Post create(UUID author, String courseId, String content, List<String> imageUrls) {
        if (courseId == null || content == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        if (imageUrls == null)
            imageUrls = new ArrayList<String>();
        return new Post(author, courseId, content, imageUrls);
    }
}
