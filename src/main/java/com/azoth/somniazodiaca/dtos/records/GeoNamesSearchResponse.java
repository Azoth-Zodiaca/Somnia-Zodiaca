package com.azoth.somniazodiaca.dtos.records;

import java.util.List;

public record GeoNamesSearchResponse(
        List<GeoNamesPlace> geonames) {
}
