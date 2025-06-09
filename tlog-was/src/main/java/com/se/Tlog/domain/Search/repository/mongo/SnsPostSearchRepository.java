package com.se.Tlog.domain.Search.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Social.Post.domain.Post;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SnsPostSearchRepository {
    private final RawSnsPostSearchRepository postSearchRepository;
    private static final ObjectId NULL_LAST_POST_ID = new ObjectId("ffffffffffffffffffffffff");
    
    public Slice<Post> searchOfDestinationsAndContent(int size, String lastPostIdStr, String queryText) {
        if (size <= 0) size = 10;
        ObjectId lastPostId = NULL_LAST_POST_ID;
        if (lastPostIdStr != null && ObjectId.isValid(lastPostIdStr)) lastPostId = new ObjectId(lastPostIdStr); 
        if (queryText == null || queryText.trim().length() <= 1)
            throw new CustomException(ErrorType.QUERY_TOO_SHORT);
        
        List<Post> results = new ArrayList<Post>(
                postSearchRepository.searchOfDestinationsAndContent(size + 1, lastPostId, queryText));
        boolean hasNext = (size + 1 == results.size());
        if (hasNext) results.remove(size);
        return new SliceImpl<Post>(results, PageRequest.ofSize(size), hasNext);
    }
}
