package com.Hackathon.minutesGamification.database.repository;

import com.Hackathon.minutesGamification.database.entities.EmployeeData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeDataRepository extends CrudRepository<EmployeeData,Integer>{
    List<EmployeeData> findByDarkstoreId(int darkstoreName);
}