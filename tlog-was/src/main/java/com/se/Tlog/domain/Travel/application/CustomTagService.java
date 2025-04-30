package com.se.Tlog.domain.Travel.application;

import com.se.Tlog.domain.Travel.domain.CustomTagDocument;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Travel.repository.mongo.CustomTagDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomTagService {
    private final CustomTagDocumentRepository customTagDocumentRepository;

    public void addCustomTag(String destinationId, List<String> tagNameList) {
        CustomTagDocument customTag = customTagDocumentRepository.findByDestinationId(destinationId)
                .orElseGet(() -> CustomTagDocument.create(destinationId));

        customTag.addOrIncrement(tagNameList);
        customTagDocumentRepository.save(customTag);
    }

    public List<TagCount> getTopTags(String destinationId, int limit) {
        return customTagDocumentRepository.findByDestinationId(destinationId)
                .map(doc -> doc.getCustomTags().stream()
                        .sorted(Comparator.comparing(TagCount::getCount).reversed())
                        .limit(limit)
                        .toList()
                ).orElse(List.of());
    }
}
