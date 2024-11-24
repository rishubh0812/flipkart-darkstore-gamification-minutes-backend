package com.Hackathon.minutesGamification.services;

import com.Hackathon.minutesGamification.database.dto.MetaData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CompetitionMetaData {

    public static final LocalDateTime competitionStartDate = LocalDateTime.of(2024, 10, 10 ,9, 0, 0);

    private static Integer getCompetitionNo(){
        LocalDateTime now = LocalDateTime.now();

        long minutesBetween = ChronoUnit.MINUTES.between(competitionStartDate, now);

        return (int) (minutesBetween / (7 * 24 * 60));
    }

    private static LocalDate getCompetitionStartDate(){
        return competitionStartDate.toLocalDate().plusWeeks(getCompetitionNo() - 1);
    }

    public static MetaData getMetaData(){
        return new MetaData(getCompetitionNo(), getCompetitionStartDate());
    }

    public static Integer getCompetitionNo(Date date) {
        // Convert the provided Date to LocalDate
        LocalDate localDate = date.toInstant()
                .atZone(ZoneId.systemDefault()) // Convert to ZonedDateTime
                .toLocalDate();

        LocalDateTime localDateTime = localDate.atStartOfDay();  // midnight of localDate

        // Calculate minutes difference between the two LocalDateTimes
        long minutesBetween = ChronoUnit.MINUTES.between(competitionStartDate, localDateTime);

        // Return the competition number based on the minutes difference
        return (int) (minutesBetween / (7 * 24 * 60)) + 1; // Assuming 7 days per competition week
    }

    public static Integer getCompetitionNo(LocalDate localDate) {
        LocalDateTime localDateTime = localDate.atStartOfDay();  // midnight of localDate

        // Calculate minutes difference between the two LocalDateTimes
        long minutesBetween = ChronoUnit.MINUTES.between(competitionStartDate, localDateTime);

        // Return the competition number based on the minutes difference
        return (int) (minutesBetween / (7 * 24 * 60)) + 1; // Assuming 7 days per competition week
    }

}
