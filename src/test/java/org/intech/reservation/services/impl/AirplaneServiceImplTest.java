package org.intech.reservation.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.response.AirplaneResponseDTO;
import org.intech.reservation.entities.Airplane;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.AirplaneMapper;
import org.intech.reservation.repositories.AirplaneRepository;
import org.intech.reservation.services.interfaces.AirplaneService;
import org.intech.reservation.validators.AirplaneValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AirplaneServiceImplTest {
    @Mock
    private AirplaneRepository airplaneRepository;
    @Mock
    private AirplaneMapper airplaneMapper;
    @InjectMocks
    private AirplaneServiceImpl airplaneService;
    Airplane airplane;
    AirplaneRequestDTO airplaneRequestDTO;
    AirplaneResponseDTO airplaneResponseDTO;
    AirplaneRequestDTO invalidRequest;
    List<String> errors;
    List<String> expectedErrors;
    InvalidEntityException exception;
    Long airplaneId = 1L;
    String model = "AIR FRANCE";
    String brand = "A750";
    Long yearMaking = 1990L;

    @BeforeEach
    void setUp(){
        airplaneRequestDTO = new AirplaneRequestDTO();
                airplaneRequestDTO.setBrand(brand);
                airplaneRequestDTO.setModel(model);
                airplaneRequestDTO.setYearMaking(yearMaking);

        airplaneResponseDTO = new AirplaneResponseDTO();
                airplaneResponseDTO.setAirplaneId(airplaneId);
                airplaneResponseDTO.setBrand(brand);
                airplaneResponseDTO.setModel(model);
                airplaneResponseDTO.setYearMaking(yearMaking);

        airplane = Airplane.builder()
                .id(airplaneId)
                .brand(brand)
                .model(model)
                .yearMaking(yearMaking)
                .build();

        invalidRequest = new AirplaneRequestDTO();
                invalidRequest.setBrand("");
                invalidRequest.setModel("");
                invalidRequest.setYearMaking(null);

        expectedErrors = Arrays.asList(
                "Can you add plane's brand",
                "Can you add plane's model",
                "Can you add year of manufacture of the airplane"
        );
    }

    @Test
    void givenAirplaneRequestDTO_whenSaveAirplane_thenReturnAirplaneObject() {
        errors = AirplaneValidator.validatePlane(airplaneRequestDTO);
        when(airplaneMapper.airplaneRequestDTOToAirplane(airplaneRequestDTO))
                .thenReturn(airplane);
        when(airplaneRepository.save(airplane))
                .thenReturn(airplane);
        when(airplaneMapper.airplaneToPlaneResponseDTO(airplane))
                .thenReturn(airplaneResponseDTO);

        AirplaneResponseDTO saveAirplaneResponse = airplaneService.savePlane(airplaneRequestDTO);
        log.info(saveAirplaneResponse.toString());

        assertThat(errors).isEmpty();
        assertThat(errors).hasSize(0);
        assertThat(saveAirplaneResponse).isNotNull();
    }

    @Test
    void givenInvalidAirplaneRequestDTO_whenValidatePlane_thenReturnErrors(){
        errors = AirplaneValidator.validatePlane(invalidRequest);

        assertThat(errors).hasSize(3);
        assertThat(errors).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenInvalidAirplaneRequestDTO_whenSaveAirplane_thenThrowsInvalidEntityException(){
        exception = assertThrows(InvalidEntityException.class, () -> {
            airplaneService.savePlane(invalidRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Airplane contains invalid data");
        assertThat(exception.getErrors()).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenAirplaneObject_whenUpdateAirplane_thenReturnAirplaneObject() {
        airplaneRequestDTO.setModel("AIR7698");
        airplaneRequestDTO.setYearMaking(1980L);
        airplaneResponseDTO.setModel("AIR7698");
        airplaneResponseDTO.setYearMaking(1980L);
        airplane.setModel("AIR7698");
        airplane.setYearMaking(1980L);

        errors = AirplaneValidator.validatePlane(airplaneRequestDTO);
        given(airplaneRepository.findById(airplaneId)).willReturn(Optional.of(airplane));
        given(airplaneRepository.save(airplane)).willReturn(airplane);

        given(airplaneMapper.airplaneToPlaneResponseDTO(airplane)).willReturn(airplaneResponseDTO);

        AirplaneResponseDTO updateAirplane = airplaneService.updatePlane(airplaneId, airplaneRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(updateAirplane.getModel()).isNotEqualTo(model);
        assertThat(updateAirplane.getYearMaking()).isNotEqualTo(yearMaking);
        assertThat(updateAirplane.getYearMaking()).isEqualTo(1980L);
        assertThat(updateAirplane.getModel()).isEqualTo("AIR7698");
    }

    @Test
    void givenAirplaneList_whenGetAllAirplane_thenReturnAirplaneResponseDTOList() {
        given(airplaneRepository.findAll()).willReturn(List.of(airplane));

        List<AirplaneResponseDTO> airplaneList = airplaneService.getAllPlane();
        log.info(airplaneList.toString());

        assertThat(airplaneList).isNotNull();
        assertThat(airplaneList.size()).isEqualTo(1);
    }

    @Test
    void givenAirplaneList_whenGetAllAirplane_thenReturnEmptyAirplaneResponseDTOList(){
        given(airplaneRepository.findAll()).willReturn(Collections.emptyList());

        List<AirplaneResponseDTO> airplaneResponseDTOList = airplaneService.getAllPlane();

        assertThat(airplaneResponseDTOList.size()).isEqualTo(0);
    }

    @Test
    void givenValidAirplaneId_whenGetExistingAirplaneById_thenReturnAirplane(){
        given(airplaneRepository.findById(airplaneId)).willReturn(Optional.of(airplane));

        Airplane result = airplaneService.getExistingAirplaneById(airplaneId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBrand()).isEqualTo(brand);
        assertThat(result.getModel()).isEqualTo(model);
        assertThat(result.getYearMaking()).isEqualTo(yearMaking);
    }

    @Test
    void givenAirplaneId_whenGetAirplaneById_thenReturnAirplaneResponseDTObject() {
        Long airplaneId = 1L;
        given(airplaneRepository.findById(airplaneId)).willReturn(Optional.of(airplane));
        given(airplaneMapper.airplaneToPlaneResponseDTO(airplane)).willReturn(airplaneResponseDTO);

        AirplaneResponseDTO saveAirplane = airplaneService.getAirplaneId(airplane.getId());

        assertThat(saveAirplane).isNotNull();
        assertThat(saveAirplane.getAirplaneId()).isEqualTo(1L);
        assertThat(saveAirplane.getBrand()).isEqualTo(brand);
        assertThat(saveAirplane.getModel()).isEqualTo(model);
        assertThat(saveAirplane.getYearMaking()).isEqualTo(yearMaking);

        verify(airplaneMapper, times(1)).airplaneToPlaneResponseDTO(airplane);
    }

    @Test
    void givenNullAirplaneId_whenGetExistingAirplaneById_thenThrowEntityNotFoundException() {
        Long airplaneWrongId = 10L;
        given(airplaneRepository.findById(airplaneWrongId)).willReturn(Optional.empty());

        assertThatThrownBy(()-> airplaneService.getExistingAirplaneById(airplaneWrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Airplane with ID = " + airplaneWrongId + " does not exist in DataBase");
    }

    @Test
    void givenAirplaneId_whenDeleteAirplane_thenReturnNothing() {
        willDoNothing().given(airplaneRepository).deleteById(airplaneId);

        airplaneService.deletePlane(airplaneId);

        verify(airplaneRepository, times(1)).deleteById(airplaneId);
    }
}