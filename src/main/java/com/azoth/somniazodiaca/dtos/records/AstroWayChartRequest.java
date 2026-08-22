package com.azoth.somniazodiaca.dtos.records;

public record AstroWayChartRequest(
        String date,
        String time,
        double timezoneOffset,
        double latitude,
        double longitude,
        String houseSystem) {
}