package org.intech.reservation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
@Builder
@Table(name = "airports")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String nameAirport;

    @Column(name = "pays", nullable = false)
    private String country;

    @Column(name = "ville", nullable = false)
    private String city;

    @OneToMany(mappedBy = "airport")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Flight> flightList;

}
