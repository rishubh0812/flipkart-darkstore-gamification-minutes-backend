package com.Hackathon.minutesGamification.services;

import com.Hackathon.minutesGamification.controller.Metrics;
import com.Hackathon.minutesGamification.database.dto.WeeklyAvgP2dMinutes;
import com.Hackathon.minutesGamification.database.entities.CityDarkstoreMapping;
import com.Hackathon.minutesGamification.database.entities.EmployeeData;
import com.Hackathon.minutesGamification.database.repository.CityDarkstoreMappingRepository;
import com.Hackathon.minutesGamification.database.repository.EmployeeDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeDataService {

    private static final Logger log = LoggerFactory.getLogger(Metrics.class);
    EmployeeDataRepository employeeDataRepository;
    CityDarkstoreMappingRepository cityDarkstoreMappingRepository;

    @Autowired
    public EmployeeDataService(EmployeeDataRepository employeeDataRepository, CityDarkstoreMappingRepository cityDarkstoreMappingRepository) {
        this.employeeDataRepository = employeeDataRepository;
        this.cityDarkstoreMappingRepository = cityDarkstoreMappingRepository;
    }

    public EmployeeDataService() {
    }

    @Cacheable("employeeData")
    public Map<Integer, List<WeeklyAvgP2dMinutes>> getEmployeeData(String darkstorId) {
        CityDarkstoreMapping cityDarkstoreMapping = cityDarkstoreMappingRepository.findByDarkstoreId(darkstorId);
        List<EmployeeData> employeeDataList = employeeDataRepository.findByDarkstoreId(cityDarkstoreMapping.getId());

        Map<String, Double> avgP2dMinutesByPickerIdAndWeek =employeeDataList.stream()
                .peek(data -> data.setWeekNumber(CompetitionMetaData.getCompetitionNo(data.getDate())))
                .collect(Collectors.groupingBy(
                data -> data.getPickerId() + "-" + data.getWeekNumber(),
                Collectors.averagingDouble(EmployeeData::getP2dMinutes)));
        return  avgP2dMinutesByPickerIdAndWeek.entrySet().stream()
                .map(entry -> {
                    String[] keyParts = entry.getKey().split("-");
                    String pickerId = keyParts[0];
                    int weekNumber = Integer.parseInt(keyParts[1]);
                    return new WeeklyAvgP2dMinutes(pickerId, weekNumber, entry.getValue());
                })
                .collect(Collectors.groupingBy(WeeklyAvgP2dMinutes::getWeekNumber));
    }

}
