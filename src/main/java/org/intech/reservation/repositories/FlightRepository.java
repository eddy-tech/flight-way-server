package org.intech.reservation.repositories;

import org.intech.reservation.entities.Airport;
import org.intech.reservation.entities.Customer;
import org.intech.reservation.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
@CrossOrigin("*")
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findFlightByCityStartAndCityArriveAndHourDateStartAndHourDateArrive(
            String cityStart,
            String cityArrive,
            String hourDateStart,
            String hourDateArrive
    );

    @Query("SELECT CASE WHEN COUNT(f)>0 THEN TRUE ELSE FALSE END FROM Flight f WHERE f.flightNumber =?1")
    Boolean existingFlightNumber (String flightNumber);

    Flight findFlightByFlightNumber(String flightNumber);
}