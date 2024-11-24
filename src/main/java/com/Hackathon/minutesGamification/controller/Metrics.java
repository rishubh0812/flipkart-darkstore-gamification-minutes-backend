package com.Hackathon.minutesGamification.controller;

import com.Hackathon.minutesGamification.services.DarkstoreLevelDataService;
import com.Hackathon.minutesGamification.services.EmployeeDataService;
import com.Hackathon.minutesGamification.services.LeaderboardScoreService;
import com.Hackathon.minutesGamification.services.WinnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/metrics")
public class Metrics {

    private static final Logger log = LoggerFactory.getLogger(Metrics.class);
    private final DarkstoreLevelDataService darkstoreLevelDataService;
    private final EmployeeDataService employeeDataService;

    private final WinnerService winnerService;

    private final LeaderboardScoreService leaderboardScoreService;

    public Metrics(DarkstoreLevelDataService darkstoreLevelDataService, EmployeeDataService employeeDataService, WinnerService winnerService, LeaderboardScoreService leaderboardScoreService) {
        this.darkstoreLevelDataService = darkstoreLevelDataService;
        this.employeeDataService = employeeDataService;
        this.winnerService = winnerService;
        this.leaderboardScoreService = leaderboardScoreService;
    }


    @GetMapping("/{country}/{state}")
    public String getMetrics() {
        return null;
    }

    @PostMapping("/update/scores")
    public void updateMetrics() {
        darkstoreLevelDataService.processScores();
    }

    @GetMapping("/employeeData/{darkstoreId}")
    public ResponseEntity<?> getEmployeeData(@PathVariable String darkstoreId) {
        log.info("Fetching employeeData");
        return ResponseEntity.status(HttpStatus.OK).body(employeeDataService.getEmployeeData(darkstoreId));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        log.info("Fetching leaderboard");
        LocalDate today = LocalDate.now();
        return ResponseEntity.status(HttpStatus.OK).body(leaderboardScoreService.getLeaderboardScores(today));
    }

    @GetMapping("/winners")
    public ResponseEntity<?> getWinners() {
        return ResponseEntity.status(HttpStatus.OK).body(winnerService.getSortedWinners());
    }

    @GetMapping("/winners/{city}")
    public ResponseEntity<?> getWinners(@PathVariable String city) {
        return ResponseEntity.status(HttpStatus.OK).body(winnerService.getSortedCityWinners(city));
    }

    @PostMapping("/update/winners")
    public void updateWinners() {
        winnerService.updateWinners();
    }
}
