package com.se.Tlog.domain.Admin.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "feedback")
@Getter
public class Feedback {
    @Id
    private String id;

    private LocalDateTime writtenAt;

    private UUID sender;

    private String title;

    private String content;

    private List<String> refImageUrls;

    private Feedback(UUID sender, String title, String content, List<String> refImageUrls) {
        this.writtenAt = LocalDateTime.now();
        this.sender = sender;
        this.title = title;
        this.content = content;
        this.refImageUrls = refImageUrls;
    }

    public static Feedback create(UUID sender, String title, String content, List<String> refImageUrls) {
        if (sender == null
            || title == null || title.trim().isBlank()
            || content == null || content.trim().isBlank())
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        if (refImageUrls == null)
            refImageUrls = new ArrayList<>();
        return new Feedback(sender, title, content, refImageUrls);
    }
}
