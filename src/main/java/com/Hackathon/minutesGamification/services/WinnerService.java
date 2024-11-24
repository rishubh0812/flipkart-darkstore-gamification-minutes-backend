package com.Hackathon.minutesGamification.services;

import com.Hackathon.minutesGamification.database.entities.City;
import com.Hackathon.minutesGamification.database.entities.LeaderboardScore;
import com.Hackathon.minutesGamification.database.entities.Winner;
import com.Hackathon.minutesGamification.database.repository.LeaderboardScoreRepository;
import com.Hackathon.minutesGamification.database.repository.WinnerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WinnerService {
    private final WinnerRepository winnerRepository;

    private final LeaderboardScoreService leaderboardScoreService;
    private final LeaderboardScoreRepository leaderboardScoreRepository;

    public WinnerService(WinnerRepository winnerRepository, LeaderboardScoreService leaderboardScoreService, LeaderboardScoreRepository leaderboardScoreRepository) {
        this.winnerRepository = winnerRepository;
        this.leaderboardScoreService = leaderboardScoreService;
        this.leaderboardScoreRepository = leaderboardScoreRepository;
    }

    @Cacheable("winners")
    public Map<Integer, List<Winner>> getSortedWinners() {
        List<Winner> winners = winnerRepository.findAll();

        Map<Integer, List<Winner>> winnersMap = new HashMap<>();
        for (Winner winner : winners) {
            winnersMap.computeIfAbsent(winner.getCompetitionNo(), k -> new ArrayList<>()).add(winner);
        }

        return sortedMap(winnersMap);
    }

    @Cacheable("winners_city")
    public Map<Integer, List<Winner>> getSortedCityWinners(String city) {
        List<Winner> winners = winnerRepository.findAll().stream().filter(x -> x.getDarkstore().getCity().getCity().equalsIgnoreCase(city)).toList();

        Map<Integer, List<Winner>> winnersMap = new HashMap<>();
        for (Winner winner : winners) {
            winnersMap.computeIfAbsent(winner.getCompetitionNo(), k -> new ArrayList<>()).add(winner);
        }
        return sortedMap(winnersMap);
    }

    private Map<Integer, List<Winner>> sortedMap(Map<Integer, List<Winner>> winnersMap) {
        Map<Integer, List<Winner>> sortedWinnersMap = winnersMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, List<Winner>>comparingByKey(Comparator.reverseOrder()))  // Descending order
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        for (List<Winner> winnerList : sortedWinnersMap.values()) {
            winnerList.sort(Comparator.comparingDouble(Winner::getScore).reversed());

            if (winnerList.size() > 3) {
                winnerList.subList(3, winnerList.size()).clear();
            }
        }

        return sortedWinnersMap;
    }

    public void updateWinners() {
        int compNo = CompetitionMetaData.getCompetitionNo(LocalDate.now()) - 1;
        List<LeaderboardScore> scores = leaderboardScoreService.getLeaderboardScoresByCompetitionNo(compNo);
        Map<Integer, Integer> allIndiaRanks = new HashMap<>();
                scores.sort(Comparator.comparingDouble(LeaderboardScore::getScore).reversed());

        Map<Integer, List<LeaderboardScore>> cityScoresMap = scores.stream()
                .collect(Collectors.groupingBy(score -> score.getDarkstore().getCity().getId()));

        IntStream.range(0, scores.size()).forEach(i -> {
            LeaderboardScore score = scores.get(i);
            allIndiaRanks.put(score.getDarkstore().getId(), i + 1);
        });

        for (Map.Entry<Integer, List<LeaderboardScore>> entry : cityScoresMap.entrySet()) {
            Integer cityId = entry.getKey();
            List<LeaderboardScore> cityScores = entry.getValue();

            cityScores.sort(Comparator.comparingDouble(LeaderboardScore::getScore).reversed());

            List<LeaderboardScore> top3Scores = cityScores.stream()
                    .limit(3)  // Take the top 3 scores
                    .toList();

            int rank = 1;
            for (LeaderboardScore top3Score : top3Scores) {
                Winner newWinner = new Winner();
                newWinner.setCompetitionNo(top3Score.getCompetitionNo());
                newWinner.setScore(top3Score.getScore());
                newWinner.setDarkstore(top3Score.getDarkstore());
                newWinner.setAllIndiaRank(allIndiaRanks.getOrDefault(top3Score.getDarkstore().getId(), Integer.MAX_VALUE));
                newWinner.setCityRank(rank);
                rank += 1;
                winnerRepository.save(newWinner);
            }
        }
    }

//    public Map<String, List<LeaderboardScore>> getTop3WinnersByCity(List<LeaderboardScore> scores) {
//        // Group the scores by city
//        Map<String, List<LeaderboardScore>> cityScoresMap = scores.stream()
//                .collect(Collectors.groupingBy(LeaderboardScore::getCity));
//
//        // For each city, sort the scores by descending score and pick the top 3
//        Map<String, List<LeaderboardScore>> top3WinnersByCity = new HashMap<>();
//
//        for (Map.Entry<String, List<LeaderboardScore>> entry : cityScoresMap.entrySet()) {
//            String city = entry.getKey();
//            List<LeaderboardScore> cityScores = entry.getValue();
//
//            // Sort the scores by score in descending order
//            cityScores.sort(Comparator.comparingInt(LeaderboardScore::getScore).reversed());
//
//            // Take the top 3 scores
//            List<LeaderboardScore> top3Scores = cityScores.stream()
//                    .limit(3)  // Take the top 3 scores
//                    .collect(Collectors.toList());
//
//            // Put the top 3 winners in the map
//            top3WinnersByCity.put(city, top3Scores);
//        }
//
//        return top3WinnersByCity;
//    }


}
