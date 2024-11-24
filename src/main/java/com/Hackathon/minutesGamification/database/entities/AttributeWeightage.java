package com.Hackathon.minutesGamification.database.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attribute_weightage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeWeightage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonManagedReference
    private City city;

    @Column(name = "attribute_name", nullable = false)
    private String attributeName;

    @Column(name = "weight", nullable = false)
    private double weight;
}
