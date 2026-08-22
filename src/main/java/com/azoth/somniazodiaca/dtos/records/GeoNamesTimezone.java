package com.azoth.somniazodiaca.dtos.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeoNamesTimezone(
        @JsonProperty("timeZoneId") String timezoneId,
        String countryCode,
        String countryName) {
}