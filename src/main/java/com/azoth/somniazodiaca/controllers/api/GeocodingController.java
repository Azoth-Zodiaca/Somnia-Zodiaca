package com.azoth.somniazodiaca.controllers.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azoth.somniazodiaca.dtos.records.LocalitaDto;
import com.azoth.somniazodiaca.services.GeocodingService;

@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/search")
    public List<LocalitaDto> search(@RequestParam String query) {
        return geocodingService.search(query);
    }
}