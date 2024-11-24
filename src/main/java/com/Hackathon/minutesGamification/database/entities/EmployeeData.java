package com.Hackathon.minutesGamification.database.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "picker_data")
@Data
public class EmployeeData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "picker_id", nullable = false)
    private String pickerId;

    @Column(name = "picker_name")
    private String pickerName;

    @Column(name = "p2d_minutes", nullable = false)
    private float p2dMinutes;

    @Column(name = "darkstore_id", nullable = false)
    private int darkstoreId;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "darkstore_id", insertable = false, updatable = false)
    private CityDarkstoreMapping cityDarkstoreMapping;

    private int weekNumber;


}
