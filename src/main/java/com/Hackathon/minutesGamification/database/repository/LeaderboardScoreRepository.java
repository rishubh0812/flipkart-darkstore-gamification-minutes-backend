package com.Hackathon.minutesGamification.database.repository;

import com.Hackathon.minutesGamification.database.entities.LeaderboardScore;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LeaderboardScoreRepository extends CrudRepository<LeaderboardScore, Long> {
    Optional<LeaderboardScore> findByDarkstoreIdAndCompetitionNo(int darkstoreId, int competitionNo);
    List<LeaderboardScore> findByCompetitionNo(int competitionNo);
}
