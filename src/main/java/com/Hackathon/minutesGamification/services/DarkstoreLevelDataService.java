package com.Hackathon.minutesGamification.services;

import com.Hackathon.minutesGamification.database.entities.AttributeWeightage;
import com.Hackathon.minutesGamification.database.entities.LeaderboardScore;
import com.Hackathon.minutesGamification.database.entities.CityDarkstoreMapping;
import com.Hackathon.minutesGamification.database.entities.DarkstoreLevelData;
import com.Hackathon.minutesGamification.database.repository.AttributeWeightageRepository;
import com.Hackathon.minutesGamification.database.repository.CityDarkstoreMappingRepository;
import com.Hackathon.minutesGamification.database.repository.DarkstoreLevelDataRepository;
import com.Hackathon.minutesGamification.database.repository.LeaderboardScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DarkstoreLevelDataService {

    private final CityDarkstoreMappingRepository cityDarkstoreMappingRepository;
    private final DarkstoreLevelDataRepository darkstoreLevelDataRepository;
    private final AttributeWeightageRepository attributeWeightageRepository;
    private final LeaderboardScoreRepository leaderboardScoreRepository;

    @Autowired
    public DarkstoreLevelDataService(CityDarkstoreMappingRepository cityDarkstoreMappingRepository,
                                     DarkstoreLevelDataRepository darkstoreLevelDataRepository,
                                     AttributeWeightageRepository attributeWeightageRepository,
                                     LeaderboardScoreRepository leaderboardScoreRepository) {
        this.cityDarkstoreMappingRepository = cityDarkstoreMappingRepository;
        this.darkstoreLevelDataRepository = darkstoreLevelDataRepository;
        this.attributeWeightageRepository = attributeWeightageRepository;
        this.leaderboardScoreRepository = leaderboardScoreRepository;
    }

    public void processScores() {
        // Get today's date and convert to java.util.Date for querying
        LocalDate today = LocalDate.now();
        today = today.minusDays(9);
        Date utilDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Fetch attribute weightages for all cities
        List<AttributeWeightage> attributeWeightages = attributeWeightageRepository.findAll();

        // Prepare a map of cityId -> attribute weights
        Map<Integer, Map<String, Double>> cityAttributeWeights = attributeWeightages.stream()
                .collect(Collectors.groupingBy(
                        attribute -> attribute.getCity().getId(),
                        Collectors.toMap(AttributeWeightage::getAttributeName, AttributeWeightage::getWeight)
                ));

        // Fetch all DarkstoreLevelData for today directly from the database
        List<DarkstoreLevelData> darkstoreLevelDataList = darkstoreLevelDataRepository.findByDate(utilDate);

        // Iterate through each darkstoreLevelData and calculate the score
        for (DarkstoreLevelData darkstoreData : darkstoreLevelDataList) {
            // Check if the score is already set
            if (darkstoreData.getPoints() != null && darkstoreData.getPoints() > 0) {
                // Skip calculation if score is already set
                continue;
            }

            // Get the corresponding cityId for this darkstore from DarkstoreLevelData
            CityDarkstoreMapping cityMapping = darkstoreData.getDarkstore();
            if (cityMapping != null) {
                Integer cityId = cityMapping.getCity().getId();  // Get cityId from the darkstore mapping

                // Get the weights for the attributes (picklist, handover, ship-to-delivered)
                Double picklistWeight = cityAttributeWeights.get(cityId).get("picklist_creation_to_dispatch");
                Double handoverWeight = cityAttributeWeights.get(cityId).get("handover_time");
                Double shipToDeliveredWeight = cityAttributeWeights.get(cityId).get("ship_to_delivered");

                // Calculate the score if all weights are available
                double score = 0;
                if (picklistWeight != null && handoverWeight != null && shipToDeliveredWeight != null) {
                    score = (darkstoreData.getNormalizedPicklistCreationToDispatch() * picklistWeight)
                            + (darkstoreData.getNormalizedHandoverTime() * handoverWeight)
                            + (darkstoreData.getNormalizedShipToDelivered() * shipToDeliveredWeight);
                }

                // Set the computed score in the points column (assuming score is scaled by 100)
                darkstoreData.setPoints((int) (score * 100));

                // Update the database with the new score
                updateDatabase(darkstoreData, score * 100, darkstoreData.getDarkstore());
            }
        }
    }

//    @Transactional
    public void updateDatabase(DarkstoreLevelData darkstoreData, double score, CityDarkstoreMapping darkstoreMapping) {
        // Step 8a: Save the updated darkstore data back to the database
        darkstoreLevelDataRepository.save(darkstoreData);

        // Step 8b: Handle Leaderboard Score Update
        Optional<LeaderboardScore> existingScoreOptional = leaderboardScoreRepository
                .findByDarkstoreIdAndCompetitionNo(darkstoreMapping.getId(), CompetitionMetaData.getCompetitionNo(darkstoreData.getDate()));

        if (existingScoreOptional.isPresent()) {
            // Update the score if the entry already exists
            LeaderboardScore existingLeaderboardScore = existingScoreOptional.get();
            existingLeaderboardScore.setScore(existingLeaderboardScore.getScore() + score);  // Add the current score to the existing one
            leaderboardScoreRepository.save(existingLeaderboardScore);
        } else {
            // If the entry does not exist, create a new one
            LeaderboardScore leaderboardScore = new LeaderboardScore();
            leaderboardScore.setDarkstore(darkstoreMapping);
            leaderboardScore.setScore(score);
            leaderboardScore.setCompetitionNo(CompetitionMetaData.getCompetitionNo(darkstoreData.getDate()));

            leaderboardScoreRepository.save(leaderboardScore);
        }
    }
}
