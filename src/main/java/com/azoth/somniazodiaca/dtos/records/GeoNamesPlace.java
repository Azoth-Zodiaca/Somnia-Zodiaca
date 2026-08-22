package com.azoth.somniazodiaca.dtos.records;

public record GeoNamesPlace(
        Long geonameId,
        String name,
        String countryName,
        String countryCode,
        String lat,
        String lng,
        GeoNamesTimezone timezone) {
}