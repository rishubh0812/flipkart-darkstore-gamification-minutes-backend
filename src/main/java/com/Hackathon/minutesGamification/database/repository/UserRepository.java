package com.Hackathon.minutesGamification.database.repository;

import com.Hackathon.minutesGamification.database.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
