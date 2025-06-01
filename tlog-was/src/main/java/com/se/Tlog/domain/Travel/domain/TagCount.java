package com.se.Tlog.domain.Travel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagCount {
    private String tagName;
    private int count;

    public void increment() {
        this.count +=1;
    }

    public static TagCount create(String tagName) {
        return new TagCount(tagName, 1);
    }
}
