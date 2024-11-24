package com.Hackathon.minutesGamification.database.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Data
public class MetaData {

    public Integer competitionNo;

    public LocalDate startDate;
}
