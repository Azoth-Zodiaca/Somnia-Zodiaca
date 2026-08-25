package com.azoth.somniazodiaca.dtos.records;

public record AstroWayInterpretationRequest(
        String date,
        String time,
        double timezoneOffset,
        double latitude,
        double longitude,
        String houseSystem,
        String language) {
}