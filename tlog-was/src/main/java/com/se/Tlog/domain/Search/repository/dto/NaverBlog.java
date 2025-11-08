package com.se.Tlog.domain.Search.repository.dto;

import lombok.Data;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class NaverBlog {
    private String title;
    private String link;
    private String description;
    private String bloggername;
    private String bloggerlink;
    private LocalDate postdate;

    public void setPostdate(String postdate) {
        this.postdate = LocalDate.parse(postdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public void setTitle(String title) {
        this.title = HtmlUtils.htmlUnescape(title).replace("<b>", "").replace("</b>", "");
    }

    public void setDescription(String description) {
        this.description = HtmlUtils.htmlUnescape(description).replace("<b>", "").replace("</b>", "");
    }
}
