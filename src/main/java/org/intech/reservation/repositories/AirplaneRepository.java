package org.intech.reservation.repositories;

import org.intech.reservation.entities.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
}