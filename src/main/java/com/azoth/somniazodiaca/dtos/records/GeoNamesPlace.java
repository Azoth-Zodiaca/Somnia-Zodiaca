package com.azoth.somniazodiaca.dtos.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoNamesPlace(
                Long geonameId,
                String name,
                String countryName,
                String countryCode,
                String lat,
                String lng,
                GeoNamesTimezone timezone) {
}