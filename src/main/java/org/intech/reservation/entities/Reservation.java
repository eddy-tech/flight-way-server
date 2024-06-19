package org.intech.reservation.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor @NoArgsConstructor
@Data
@Table(name = "reservations")
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "booked_flight")
    private Flight bookedFlight;
    @ManyToOne
    @JoinColumn(name = "booked_by")
    private Customer bookedBy;
}
