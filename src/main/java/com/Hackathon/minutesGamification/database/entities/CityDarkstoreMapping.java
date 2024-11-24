package com.Hackathon.minutesGamification.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "city_darkstore_mappings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityDarkstoreMapping {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonManagedReference
    private City city;

    @Column(name = "darkstore_id", nullable = false)
    private String darkstoreId;

    @Column(name = "darkstore_name", nullable = false)
    private String darkstoreName;
}
