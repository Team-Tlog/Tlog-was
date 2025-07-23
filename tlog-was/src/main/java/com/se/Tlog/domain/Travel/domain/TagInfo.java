package com.se.Tlog.domain.Travel.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TagInfo { // 여행지가 갖고 있는 태그 정보
    private String id;
    private int weight;
    private boolean isDeleted;
    
    // 추후 CreateFixedTagDto로 입력받도록 변경 예정
    public static TagInfo create(String tagId, int tagWeight) {
    	/*if (!validator.existById(tagId))
    		throw new CustomException(ErrorType.NOT_FOUND_TAG);
		* // 해당 태그가 존재하는지 여부를 검사하는 일은 여기가 아닌, 응용 계층에서 이미 완료되어야 할 것임.
		*/
        return new TagInfo(tagId, tagWeight, false);
    }
}
