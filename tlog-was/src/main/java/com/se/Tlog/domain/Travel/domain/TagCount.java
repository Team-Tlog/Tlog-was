package com.se.Tlog.domain.Travel.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
