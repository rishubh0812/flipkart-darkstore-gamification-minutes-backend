package com.Hackathon.minutesGamification.database.repository;

import com.Hackathon.minutesGamification.database.entities.Winner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WinnerRepository extends CrudRepository<Winner, Integer> {

    @Override
    List<Winner> findAll();
}
