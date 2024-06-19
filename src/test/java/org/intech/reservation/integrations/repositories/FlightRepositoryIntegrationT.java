package org.intech.reservation.integrations.repositories;

import org.intech.reservation.entities.Airplane;
import org.intech.reservation.entities.Flight;
import org.intech.reservation.integrations.containers.AbstractionContainerBaseTest;
import org.intech.reservation.repositories.AirplaneRepository;
import org.intech.reservation.repositories.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FlightRepositoryIntegrationT extends AbstractionContainerBaseTest {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirplaneRepository airplaneRepository;
    Flight flight;
    Airplane airplane;

    @BeforeEach
    void setUp() {
        airplane = Airplane.builder()
                .brand("AIR FRANCE")
                .model("A750")
                .yearMaking(1990L)
                .build();

        flight = Flight.builder()
                .flightNumber("15B25F10")
                .cityStart("Paris")
                .cityArrive("Douala")
                .hourDateStart("07:10:00")
                .hourDateArrive("19:30:00")
                .numberOfPlace(350L)
                .airplane(airplane)
                .build();
    }

    @Test
    void givenCityStartAndCityArriveAndHourDateStartAndHourDateArrive_whenFindByParams_thenReturnFlightObject() {
        airplaneRepository.save(airplane);
        flightRepository.save(flight);

        List<Flight> saveFlight = flightRepository.findFlightByCityStartAndCityArriveAndHourDateStartAndHourDateArrive(
                flight.getCityStart(),
                flight.getCityArrive(),
                flight.getHourDateStart(),
                flight.getHourDateArrive()
        );

        assertThat(saveFlight).isNotNull();
    }

    @Test
    void givenFlightNumber_whenExistingFlightNumber_thenReturnBoolean() {
        airplaneRepository.save(airplane);
        flightRepository.save(flight);

        Boolean existingFlight = flightRepository.existingFlightNumber(flight.getFlightNumber());
        Boolean notExistingFlight = flightRepository.existingFlightNumber("B152FL56");

        assertThat(existingFlight).isTrue();
        assertThat(notExistingFlight).isFalse();
    }

    @Test
    void findFlightByFlightNumber() {
        airplaneRepository.save(airplane);
        flightRepository.save(flight);

        Flight saveFlight = flightRepository.findFlightByFlightNumber(flight.getFlightNumber());

        assertThat(saveFlight).isNotNull();
    }
}
