package com.Hackathon.minutesGamification.database.dto;


import com.Hackathon.minutesGamification.database.entities.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    private int id;
    private City city;
    private String name;
    private double weight;
}
