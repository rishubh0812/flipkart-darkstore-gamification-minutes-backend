package com.Hackathon.minutesGamification.database.repository;

import com.Hackathon.minutesGamification.database.entities.AttributeWeightage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttributeWeightageRepository extends CrudRepository<AttributeWeightage, Integer> {
    List<AttributeWeightage> findAll();
}
