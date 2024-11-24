package com.Hackathon.minutesGamification.database.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leaderboard_score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "darkstore_id", nullable = false)
    @JsonManagedReference
    private CityDarkstoreMapping darkstore;

    @Column(name = "score", nullable = false)
    private double score;

    @Column(name = "competition_no", nullable = false)
    private int competitionNo;
}
