package com.Hackathon.minutesGamification.database.repository;

import com.Hackathon.minutesGamification.database.entities.CityDarkstoreMapping;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityDarkstoreMappingRepository extends CrudRepository<CityDarkstoreMapping, Integer> {
    List<CityDarkstoreMapping> findAll();

    CityDarkstoreMapping findByDarkstoreId(String darkstoreId);
}
