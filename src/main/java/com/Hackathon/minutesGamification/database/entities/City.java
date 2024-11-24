package com.Hackathon.minutesGamification.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "city", nullable = false, unique = true)
    private String city;

//    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
////    @JsonBackReference
//    private List<CityDarkstoreMapping> cityDarkstoreMappings;
//
//    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
////    @JsonBackReference
//    private List<AttributeWeightage> attributeWeightages;
}
