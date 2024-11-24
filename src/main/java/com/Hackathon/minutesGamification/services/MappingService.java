package com.Hackathon.minutesGamification.services;

import com.Hackathon.minutesGamification.database.entities.City;
import com.Hackathon.minutesGamification.database.repository.AttributeWeightageRepository;
import com.Hackathon.minutesGamification.database.repository.CityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MappingService {

    private final CityRepository citiesRepository;

    @Autowired
    public MappingService(CityRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    @Transactional
    public List<String> getAllCities() {
        List<String> cities = new ArrayList<>();
        for (City c : citiesRepository.findAll()) {
            cities.add(c.getCity());
        }
        return cities;
    }
}
