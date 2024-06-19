package org.intech.reservation.services.impl;

import lombok.extern.slf4j.Slf4j;

import org.intech.reservation.dtos.request.FlightRequestDTO;

import org.intech.reservation.dtos.response.FlightResponseDTO;
import org.intech.reservation.entities.Airplane;
import org.intech.reservation.entities.Airport;
import org.intech.reservation.entities.Flight;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.FlightMapper;
import org.intech.reservation.repositories.FlightRepository;
import org.intech.reservation.validators.FlightValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private FlightMapper flightMapper;
    @InjectMocks
    private FlightServiceImpl flightService;
    FlightResponseDTO flightResponseDTO;
    FlightRequestDTO flightRequestDTO;
    FlightRequestDTO invalidRequest;
    List<String> expectedErrors;
    InvalidEntityException exception;
    Flight flight;
    Airplane airplane;
    Airport airport;
    List<String> errors;
    Long flightId = 1L;
    String cityStart = "Paris";
    String cityArrive = "Douala";
    Long numberOfPlace = 350L;
    String hourDateStart = "23:30:00";
    String hourDateArrive = "07:15:00";
    String flightNumber = "BTH458L90";

    Long airplaneId = 1L;
    String model = "AIR FRANCE";
    String brand = "A750";
    Long yearMaking = 1990L;

    Long airportId = 1L;
    String nameAirport = "Charles de Gaulle";
    String nameCity = "Paris";
    String country = "France";


    @BeforeEach
    void setUp() {
        airplane = Airplane.builder()
                .id(airplaneId)
                .brand(brand)
                .model(model)
                .yearMaking(yearMaking)
                .build();

        airport = Airport.builder()
                .id(airportId)
                .nameAirport(nameAirport)
                .city(nameCity)
                .country(country)
                .build();

        flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.setCityStart(cityStart);
        flightResponseDTO.setCityArrive(cityArrive);
        flightResponseDTO.setNumberOfPlace(numberOfPlace);
        flightResponseDTO.setHourDateArrive(hourDateArrive);
        flightResponseDTO.setHourDateStart(hourDateStart);
        flightResponseDTO.setFlightNumber(flightNumber);
        flightResponseDTO.setAirplane(airplane);
        flightResponseDTO.setAirport(airport);

        flightRequestDTO = new FlightRequestDTO();
        flightRequestDTO.setCityStart(cityStart);
        flightRequestDTO.setCityArrive(cityArrive);
        flightRequestDTO.setNumberOfPlace(numberOfPlace);
        flightRequestDTO.setHourDateArrive(hourDateArrive);
        flightRequestDTO.setHourDateStart(hourDateStart);

        flight = new Flight();
        flight.setId(flightId);
        flight.setCityStart(cityStart);
        flight.setCityArrive(cityArrive);
        flight.setNumberOfPlace(numberOfPlace);
        flight.setHourDateArrive(hourDateArrive);
        flight.setHourDateStart(hourDateStart);
        flight.setFlightNumber(flightNumber);
        flight.setAirplane(airplane);
        flight.setAirport(airport);

        invalidRequest = new FlightRequestDTO();
        invalidRequest.setCityStart("");
        invalidRequest.setCityArrive("");
        invalidRequest.setNumberOfPlace(null);
        invalidRequest.setHourDateArrive("");
        invalidRequest.setHourDateStart("");

        expectedErrors = Arrays.asList(
                "Can you add departure city",
                "Can you add city of arrival",
                "Can you add number of place",
                "Can you add hour of departure date",
                "Can you add hour of arrival date"
        );
    }

    @Test
    void givenFlightRequestDTO_whenSaveFlightId_thenReturnFlightResponseDTO() {
        errors = FlightValidator.validateFlight(flightRequestDTO);
        when(flightMapper.flightRequestDTOToFlight(flightRequestDTO)).thenReturn(flight);
        when(flightRepository.save(flight)).thenReturn(flight);
        when(flightMapper.flightToFlightResponseDTO(flight)).thenReturn(flightResponseDTO);

        FlightResponseDTO saveFlight = flightService.saveFlight(flightRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(errors).hasSize(0);
        assertThat(saveFlight).isNotNull();
    }

    @Test
    void givenInvalidFlightRequestDTO_whenValidateFlight_thenReturnErrors(){
        errors = FlightValidator.validateFlight(invalidRequest);

        assertThat(errors).hasSize(5);
        assertThat(errors).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenInvalidFlightRequestDTO_whenSaveFlight_thenThrowsInvalidEntityException(){
        exception = assertThrows(InvalidEntityException.class, () -> {
            flightService.saveFlight(invalidRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Flight is invalid");
        assertThat(exception.getErrors()).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenFlightParams_whenSearchFlight_thenReturnThrowsInvalidEntityException(){
         cityArrive = cityStart;

         exception = assertThrows(InvalidEntityException.class, ()-> {
             flightService.searchFlight(cityStart, cityArrive, hourDateStart, hourDateArrive);
         });

         assertThat(exception.getMessage()).isEqualTo(
                 "Cities don't have to be the same - start = " + cityStart + " arrive = "+ cityArrive);
         assertThat(HttpStatus.NOT_ACCEPTABLE).isEqualTo(exception.getHttpStatus());
    }

    @Test
    void givenFlightRequestDTO_whenUpdateFlight_thenReturnFlightResponseDTO() {
        flightRequestDTO.setCityArrive("Yaounde");
        flightRequestDTO.setCityStart("Istanbul");
        flightResponseDTO.setCityArrive("Yaounde");
        flightResponseDTO.setCityStart("Istanbul");
        flight.setCityArrive("Yaounde");
        flight.setCityStart("Istanbul");

        errors = FlightValidator.validateFlight(flightRequestDTO);
        given(flightRepository.findById(flightId)).willReturn(Optional.of(flight));
        given(flightRepository.save(flight)).willReturn(flight);

        given(flightMapper.flightToFlightResponseDTO(flight)).willReturn(flightResponseDTO);

        FlightResponseDTO updateFlight = flightService.updateFlight(flightId, flightRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(updateFlight.getCityStart()).isNotEqualTo(cityStart);
        assertThat(updateFlight.getCityArrive()).isNotEqualTo(cityArrive);
        assertThat(updateFlight.getCityStart()).isEqualTo("Istanbul");
        assertThat(updateFlight.getCityArrive()).isEqualTo("Yaounde");
    }

    @Test
    void givenFlightId_whenGetExistingEmployeeId_thenReturnEmployeeResponseDTO() {
        given(flightRepository.findById(flightId)).willReturn(Optional.of(flight));

        Flight result = flightService.getExistingFlightId(flightId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFlightNumber()).isEqualTo(flightNumber);
        assertThat(result.getCityStart()).isEqualTo(cityStart);
        assertThat(result.getCityArrive()).isEqualTo(cityArrive);
        assertThat(result.getHourDateStart()).isEqualTo(hourDateStart);
        assertThat(result.getHourDateArrive()).isEqualTo(hourDateArrive);
        assertThat(result.getAirplane()).isEqualTo(airplane);
        assertThat(result.getAirport()).isEqualTo(airport);
    }

    @Test
    void givenFlightList_whenGetAllFlight_thenReturnFlightResponseList() {
        given(flightRepository.findAll()).willReturn(List.of(flight));

        List<FlightResponseDTO> flightList = flightService.getAllFlight();
        log.info(flightList.toString());

        assertThat(flightList).isNotNull();
        assertThat(flightList.size()).isEqualTo(1);
    }

    @Test
    void givenFlightRequestDTO_whenGetAllFlight_thenReturnFlightResponseDTOList() {
        given(flightRepository.findAll()).willReturn(Collections.emptyList());

        List<FlightResponseDTO> employeeList = flightService.getAllFlight();

        assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    void givenFlightDataParams_whenSearchFlight_thenReturnAllFlightResponseDTOList(){
        List<Flight> flightList = new ArrayList<Flight>();
        flightList.add(flight);
        given(flightRepository
                .findFlightByCityStartAndCityArriveAndHourDateStartAndHourDateArrive
                        (cityStart, cityArrive, hourDateStart, hourDateArrive))
                .willReturn(flightList);

        given(flightMapper.flightToFlightResponseDTO(flight)).willReturn(flightResponseDTO);

        List<FlightResponseDTO> resultList = flightService.searchFlight(cityStart, cityArrive, hourDateStart, hourDateArrive);

        assertThat(resultList.size()).isEqualTo(1);
        FlightResponseDTO responseDTO = resultList.get(0);
        assertThat(flightResponseDTO.getFlightNumber()).isEqualTo(responseDTO.getFlightNumber());
        assertThat(flightResponseDTO.getCityArrive()).isEqualTo(responseDTO.getCityArrive());
        assertThat(flightResponseDTO.getCityStart()).isEqualTo(responseDTO.getCityStart());
        assertThat(flightResponseDTO.getNumberOfPlace()).isEqualTo(responseDTO.getNumberOfPlace());
    }



    @Test
    void givenFlightRequestDTO_GetFlightId_thenReturnFlightResponseDTO() {
        given(flightRepository.findById(flightId)).willReturn(Optional.of(flight));
        given(flightMapper.flightToFlightResponseDTO(flight)).willReturn(flightResponseDTO);

        FlightResponseDTO saveFlight = flightService.getFlightId(flight.getId());

        assertThat(saveFlight).isNotNull();
        assertThat(saveFlight.getFlightNumber()).isEqualTo(flightNumber);
        assertThat(saveFlight.getCityStart()).isEqualTo(cityStart);
        assertThat(saveFlight.getCityArrive()).isEqualTo(cityArrive);
        assertThat(saveFlight.getNumberOfPlace()).isEqualTo(numberOfPlace);
        assertThat(saveFlight.getHourDateStart()).isEqualTo(hourDateStart);
        assertThat(saveFlight.getHourDateArrive()).isEqualTo(hourDateArrive);
        assertThat(saveFlight.getAirplane()).isEqualTo(airplane);
        assertThat(saveFlight.getAirport()).isEqualTo(airport);

        verify(flightMapper, times(1)).flightToFlightResponseDTO(flight);
    }

    @Test
    void givenNullFlightId_whenGetExistingFlightById_thenThrowEntityNotFoundException() {
        Long flightWrongId = 10L;
        given(flightRepository.findById(flightWrongId)).willReturn(Optional.empty());

        assertThatThrownBy(()-> flightService.getExistingFlightId(flightWrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Flight with Id = " + flightWrongId + " does not exist in DataBase");
    }

    @Test
    void givenFlightId_whenDeleteFlight_thenReturnNothing() {
        willDoNothing().given(flightRepository).deleteById(flightId);

        flightService.deleteFlight(flightId);

        verify(flightRepository, times(1)).deleteById(flightId);
    }
}