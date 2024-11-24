package com.Hackathon.minutesGamification.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "darkstore_level_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DarkstoreLevelData {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "darkstore_id", nullable = false)
    @JsonManagedReference
    private CityDarkstoreMapping darkstore;

    @Column(name = "order_value", nullable = false)
    private Double orderValue;

    @Column(name = "sla")
    private Double sla;

    @Column(name = "picklist_creation_to_dispatch")
    private double picklistCreationToDispatch;

    @Column(name = "handover_time")
    private double handoverTime;

    @Column(name = "ship_to_delivered")
    private double shipToDelivered;

    @Column(name = "unit_cont")
    private Integer unitCont;

    @Column(name = "unit_fsn_count")
    private Integer unitFsnCount;

    @Column(name = "normalized_picklist_creation_to_dispatch")
    private double normalizedPicklistCreationToDispatch;

    @Column(name = "normalized_handover_time")
    private double normalizedHandoverTime;

    @Column(name = "normalized_ship_to_delivered")
    private double normalizedShipToDelivered;

    @Column(name = "date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

    @Column(name="points")
    private Integer points;
}
