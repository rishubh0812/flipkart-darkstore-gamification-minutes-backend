package com.Hackathon.minutesGamification.services;

import com.Hackathon.minutesGamification.controller.Metrics;
import com.Hackathon.minutesGamification.database.entities.LeaderboardScore;
import com.Hackathon.minutesGamification.database.repository.LeaderboardScoreRepository;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class LeaderboardScoreService {

    private final LeaderboardScoreRepository leaderboardScoreRepository;
    private static final Logger log = LoggerFactory.getLogger(Metrics.class);


    public LeaderboardScoreService(LeaderboardScoreRepository leaderboardScoreRepository) {
        this.leaderboardScoreRepository = leaderboardScoreRepository;
    }


    @Cacheable("leaderboard")
    public List<LeaderboardScore> getLeaderboardScores(LocalDate date) {
        int competitionNo = CompetitionMetaData.getCompetitionNo(date);
        log.info("db start");

        List<LeaderboardScore> leaderboardScores = leaderboardScoreRepository.findByCompetitionNo(competitionNo);
        log.info("db end");

        leaderboardScores.sort(Comparator.comparingDouble(LeaderboardScore::getScore).reversed());
        log.info("done");


        return leaderboardScores;
    }

    public List<LeaderboardScore> getLeaderboardScoresByCompetitionNo(int competitionNo) {
        return leaderboardScoreRepository.findByCompetitionNo(competitionNo);
    }
}
