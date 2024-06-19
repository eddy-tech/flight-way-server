package org.intech.reservation.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.request.AirportRequestDTO;
import org.intech.reservation.dtos.response.AirplaneResponseDTO;
import org.intech.reservation.dtos.response.AirportResponseDTO;
import org.intech.reservation.entities.Airplane;
import org.intech.reservation.entities.Airport;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.AirplaneMapper;
import org.intech.reservation.mappers.AirportMapper;
import org.intech.reservation.repositories.AirplaneRepository;
import org.intech.reservation.repositories.AirportRepository;
import org.intech.reservation.services.interfaces.AirplaneService;
import org.intech.reservation.validators.AirplaneValidator;
import org.intech.reservation.validators.AirportValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {
    @Mock
    private AirportRepository airportRepository;
    @Mock
    private AirportMapper airportMapper;
    @InjectMocks
    private AirportServiceImpl airportService;
    AirportResponseDTO airportResponseDTO;
    AirportRequestDTO airportRequestDTO;
    AirportRequestDTO invalidRequest;
    List<String> expectedErrors;
    InvalidEntityException exception;
    Airport airport;
    List<String> errors;
    Long airportId = 1L;
    String nameAirport = "Charles de Gaulle";
    String nameCity = "Paris";
    String country = "France";


    @BeforeEach
    void setUp() {
        airportRequestDTO = new AirportRequestDTO();
           airportRequestDTO.setNameAirport(nameAirport);
           airportRequestDTO.setCity(nameCity);
           airportRequestDTO.setCountry(country);

        airportResponseDTO = new AirportResponseDTO();
           airportResponseDTO.setAirportId(airportId);
           airportResponseDTO.setNameAirport(nameAirport);
           airportResponseDTO.setCity(nameCity);
           airportResponseDTO.setCountry(country);

        airport = Airport.builder()
                .id(1L)
                .nameAirport(nameAirport)
                .city(nameCity)
                .country(country)
                .build();

        invalidRequest = new AirportRequestDTO();
           invalidRequest.setNameAirport("");
           invalidRequest.setCity("");
           invalidRequest.setCountry("");

        expectedErrors = Arrays.asList(
                "Can you add Airport name",
                "Can you please add airport's country",
                "Can you please add airport's city"
        );
    }

    @Test
    void givenAirportRequestDTO_whenSaveAirportResponseDTO_thenReturnAirportResponseObject() {
        errors = AirportValidator.validateAirport(airportRequestDTO);
        when(airportMapper.airportRequestDTOToAirport(airportRequestDTO)).thenReturn(airport);
        when(airportRepository.save(airport)).thenReturn(airport);
        when(airportMapper.airportToAirportResponseDTO(airport)).thenReturn(airportResponseDTO);

        AirportResponseDTO saveAirport = airportService.saveAirport(airportRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(errors).hasSize(0);
        assertThat(saveAirport).isNotNull();
    }

    @Test
    void givenInvalidAirportRequestDTO_whenValidateAirport_thenReturnErrors(){
        errors = AirportValidator.validateAirport(invalidRequest);

        assertThat(errors).hasSize(3);
        assertThat(errors).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenInvalidAirportRequestDTO_whenSaveAirport_thenThrowsInvalidEntityException(){
        exception = assertThrows(InvalidEntityException.class, () -> {
            airportService.saveAirport(invalidRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Airport contains invalid data");
        assertThat(exception.getErrors()).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenAirportRequestDTO_whenUpdateAirportResponseDTO_thenReturnAirportResponseObject() {
        airportRequestDTO.setCountry("Cameroon");
        airportRequestDTO.setCity("Yaounde");
        airportResponseDTO.setCountry("Cameroon");
        airportResponseDTO.setCity("Yaounde");
        airport.setCountry("Cameroon");
        airport.setCity("Yaounde");

        errors = AirportValidator.validateAirport(airportRequestDTO);
        given(airportRepository.findById(airportId)).willReturn(Optional.of(airport));
        given(airportRepository.save(airport)).willReturn(airport);

        given(airportMapper.airportToAirportResponseDTO(airport)).willReturn(airportResponseDTO);

        AirportResponseDTO updateAirport = airportService.updateAirport(airportId, airportRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(updateAirport.getCountry()).isNotEqualTo(country);
        assertThat(updateAirport.getCity()).isNotEqualTo(nameCity);
        assertThat(updateAirport.getCountry()).isEqualTo("Cameroon");
        assertThat(updateAirport.getCity()).isEqualTo("Yaounde");
    }

    @Test
    void givenAirportId_whenGetExistingAirportId_thenReturnAirportResponseObject() {
        given(airportRepository.findById(airportId)).willReturn(Optional.of(airport));

        Airport result = airportService.getExistingAirportId(airportId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNameAirport()).isEqualTo(nameAirport);
        assertThat(result.getCity()).isEqualTo(nameCity);
        assertThat(result.getCountry()).isEqualTo(country);
    }

    @Test
    void givenAirportList_whenGetAllAirport_thenReturnAirportResponseList() {
        given(airportRepository.findAll()).willReturn(List.of(airport));

        List<AirportResponseDTO> airportList = airportService.getAllAirport();
        log.info(airportList.toString());

        assertThat(airportList).isNotNull();
        assertThat(airportList.size()).isEqualTo(1);
    }

    @Test
    void givenAirportList_whenGetAllAirport_thenReturnEmptyAirportResponseDTOList(){
        given(airportRepository.findAll()).willReturn(Collections.emptyList());

        List<AirportResponseDTO> airplaneResponseDTOList = airportService.getAllAirport();

        assertThat(airplaneResponseDTOList.size()).isEqualTo(0);
    }

    @Test
    void givenAirportId_whenGetAirportId_thenReturnAirportResponseObject() {
        given(airportRepository.findById(airportId)).willReturn(Optional.of(airport));
        given(airportMapper.airportToAirportResponseDTO(airport)).willReturn(airportResponseDTO);

        AirportResponseDTO saveAirport = airportService.getAirportId(airport.getId());

        assertThat(saveAirport).isNotNull();
        assertThat(saveAirport.getAirportId()).isEqualTo(1L);
        assertThat(saveAirport.getCity()).isEqualTo(nameCity);
        assertThat(saveAirport.getCountry()).isEqualTo(country);

        verify(airportMapper, times(1)).airportToAirportResponseDTO(airport);
    }

    @Test
    void givenNullAirportId_whenGetExistingAirportById_thenThrowEntityNotFoundException() {
        Long airplaneWrongId = 10L;
        given(airportRepository.findById(airplaneWrongId)).willReturn(Optional.empty());

        assertThatThrownBy(()-> airportService.getExistingAirportId(airplaneWrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Airport with ID = " + airplaneWrongId + " does not exist in DataBase");
    }

    @Test
    void givenAirportId_whenDeleteAirport_thenReturnNothing() {
        willDoNothing().given(airportRepository).deleteById(airportId);

        airportService.deleteAirport(airportId);

        verify(airportRepository, times(1)).deleteById(airportId);
    }
}