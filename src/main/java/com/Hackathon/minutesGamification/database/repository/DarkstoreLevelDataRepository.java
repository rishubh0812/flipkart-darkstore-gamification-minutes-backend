package com.Hackathon.minutesGamification.database.repository;

import com.Hackathon.minutesGamification.database.entities.DarkstoreLevelData;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DarkstoreLevelDataRepository extends CrudRepository<DarkstoreLevelData, Integer> {
    Optional<DarkstoreLevelData> findByDarkstoreIdAndDate(int darkstoreId, Date date);
    List<DarkstoreLevelData> findByDate(Date date);
}
