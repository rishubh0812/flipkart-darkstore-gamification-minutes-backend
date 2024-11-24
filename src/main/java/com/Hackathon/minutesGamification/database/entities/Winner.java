package com.Hackathon.minutesGamification.database.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "winners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Winner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "competition_no", nullable = false)
    private int competitionNo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "darkstore_id", nullable = false)
    @JsonManagedReference
    private CityDarkstoreMapping darkstore;

    @Column(name = "score", nullable = false)
    private double score;

    @Column(name = "all_india_rank")
    private int allIndiaRank;

    @Column(name = "city_rank")
    private int cityRank;
}

