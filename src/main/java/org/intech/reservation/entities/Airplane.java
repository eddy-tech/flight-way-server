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
@Table(name = "airplanes")
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marque", nullable = false)
    private String brand;

    @Column(name = "models", nullable = false)
    private String model;

    @Column(name = "annee_fabrication", nullable = false, length = 4)
    private Long yearMaking;

    @OneToMany(mappedBy = "airplane", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Flight> flightList;
}
