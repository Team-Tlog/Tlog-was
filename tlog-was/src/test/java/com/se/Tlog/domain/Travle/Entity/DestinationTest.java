/*
package com.se.Tlog.domain.Travle.Entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.se.Tlog.domain.Travel.Entity.Destination;
import com.se.Tlog.domain.Travel.Entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class DestinationTest {
	@Autowired
	private JpaRepository<Destination, Long> destinationRepository;

	@Autowired
	private JpaRepository<DestinationTag, DestinationTagPK> desTagRepository;

	@Autowired
	private JpaRepository<Tag, Long> tagRepository;

	@DisplayName("여행지 등록 테스트")
	@Test
	void destinationGenerate() {
		// Given
		Destination destination = Destination.builder()
				.location("여행지 주소")
				.name("여행지 이름")
				.rating(5)
				.build();
		Tag tag1 = Tag.builder().name("테스트 태그1").build();
		Tag tag2 = Tag.builder().name("테스트 태그2").build();
		Tag tag3 = Tag.builder().name("테스트 태그3").build();

		// When
		destination.addTag(tag1);
		destination.addTag(tag2);
		destination.addTag(tag3);

		tagRepository.save(tag1);
		tagRepository.save(tag2);
		tagRepository.save(tag3);

		destinationRepository.save(destination);

		desTagRepository.saveAll(destination.getTagList());

		// Then
		assertThat(tagRepository.findById(tag1.getId())).isNotNull();
		assertThat(tagRepository.findById(tag2.getId())).isNotNull();
		assertThat(tagRepository.findById(tag3.getId())).isNotNull();

		assertThat(destinationRepository.findById(destination.getId())).isNotNull();

		assertThat(desTagRepository.findById(
				new DestinationTagPK(
						destination.getId(),
						tag1.getId()))
				).isNotNull();
		assertThat(desTagRepository.findById(
				new DestinationTagPK(
						destination.getId(),
						tag2.getId()))
				).isNotNull();
		assertThat(desTagRepository.findById(
				new DestinationTagPK(
						destination.getId(),
						tag3.getId()))
				).isNotNull();
	}
}
*/
