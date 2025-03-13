package com.se.Tlog.domain.Travel.dto;

import com.se.Tlog.domain.Travel.Entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    private String name;

    public static Tag toEntity(TagDto tagDto) {
        return Tag.builder()
                .name(tagDto.getName())
                .build();
    }
}
