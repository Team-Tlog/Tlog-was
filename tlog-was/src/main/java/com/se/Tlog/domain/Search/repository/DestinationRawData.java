package com.se.Tlog.domain.Search.repository;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "destinations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DestinationRawData {
	@Id
	private String id;
	private String name;
	private String address;
}
