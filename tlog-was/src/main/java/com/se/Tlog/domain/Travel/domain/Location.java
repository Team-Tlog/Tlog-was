package com.se.Tlog.domain.Travel.domain;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {
    private String longitude;
    private String latitude;
}
