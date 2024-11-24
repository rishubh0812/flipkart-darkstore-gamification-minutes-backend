package com.Hackathon.minutesGamification.controller;


import com.Hackathon.minutesGamification.services.CompetitionMetaData;
import com.Hackathon.minutesGamification.services.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/get")
@CrossOrigin(origins = "http://localhost:3000")
public class RegionMapping {

    private final MappingService mappingService;

    @Autowired
    public RegionMapping(MappingService mappingService) {
        this.mappingService = mappingService;
    }

    @GetMapping("/cities")
    @Cacheable("cities")
    public ResponseEntity<List<String>> getAllCities() {
        return ResponseEntity.status(HttpStatus.OK).body(mappingService.getAllCities());
    }

    @GetMapping("/metadata")
    public ResponseEntity<?> getCompMetadata() {
        return ResponseEntity.status(HttpStatus.OK).body(CompetitionMetaData.getMetaData());
    }
}
