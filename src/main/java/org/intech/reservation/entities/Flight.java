package org.intech.reservation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
@Builder
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "num_flight", unique = true)
    private String flightNumber;

    @Column(nullable = false, name = "ville_depart")
    private String cityStart;

    @Column(nullable = false, name = "ville_arrivee")
    private String cityArrive;

    @Column(nullable = false, name = "heure_date_depart")
    private String hourDateStart;

    @Column(nullable = false, name = "heure_date_arrivee")
    private String hourDateArrive;

    @Column(nullable = false, name = "number_of_place")
    private Long numberOfPlace;

    @ManyToOne
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;
    @ManyToOne
    @JoinColumn(name = "airport_id")
    private Airport airport;

}
