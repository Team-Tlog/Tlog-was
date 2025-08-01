package com.se.Tlog.domain.Admin.domain;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "report_post")
@Getter
@NoArgsConstructor
public class ReportOfPost {
    @Id
    private String id;

    private String postId;

    private List<ReportData> reports;

    @Getter
    public static class ReportData {
        private UUID reporterId;

        private LocalDateTime reportedAt;

        /* // 추후 확장시 본문도 포함가능합니다.
        private String title;

        private String content;
         */

        private ReportData(UUID reporterId) {
            this.reporterId = reporterId;
            this.reportedAt = LocalDateTime.now();
        }
    }

    public void addReport(UUID reporterId) {
        this.reports.add(new ReportData(reporterId));
    }

    private ReportOfPost(String postId, UUID reporterId) {
        this.postId = postId;
        this.reports = new ArrayList<>();
        addReport(reporterId);
    }

    public static ReportOfPost create(String postId, UUID reporterId) {
        if (postId == null || reporterId == null)
            throw new CustomException(ErrorType.ILLEGAL_ARGUMENT);
        return new ReportOfPost(postId, reporterId);
    }
}
