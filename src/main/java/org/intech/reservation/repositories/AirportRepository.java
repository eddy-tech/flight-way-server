package org.intech.reservation.repositories;

import org.intech.reservation.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public interface AirportRepository extends JpaRepository<Airport, Long> {
}