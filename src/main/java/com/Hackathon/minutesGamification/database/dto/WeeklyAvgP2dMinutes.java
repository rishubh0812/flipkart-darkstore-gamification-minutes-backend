package com.Hackathon.minutesGamification.database.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyAvgP2dMinutes {
    private String pickerId;
    private int weekNumber;
    private double avgP2dMinutes;
}
