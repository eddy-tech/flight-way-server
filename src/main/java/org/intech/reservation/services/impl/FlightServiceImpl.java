package org.intech.reservation.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.FlightRequestDTO;
import org.intech.reservation.dtos.response.FlightResponseDTO;
import org.intech.reservation.entities.Flight;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.AirplaneMapper;
import org.intech.reservation.mappers.FlightMapper;
import org.intech.reservation.repositories.FlightRepository;
import org.intech.reservation.services.interfaces.FlightService;
import org.intech.reservation.validators.FlightValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {
    private FlightRepository flightRepository;
    private FlightMapper flightMapper;

    @Override
    public FlightResponseDTO saveFlight(FlightRequestDTO flightRequestDto) {
        List<String> errors = FlightValidator.validateFlight(flightRequestDto);
        if(!errors.isEmpty()){
            log.error("Flight is invalid");
            throw new InvalidEntityException("Flight is invalid", errors);
        }

        Flight flight = flightMapper.flightRequestDTOToFlight(flightRequestDto);
        flight.setFlightNumber(UUID.randomUUID().toString());
        Flight saveFlight = flightRepository.save(flight);

        return flightMapper.flightToFlightResponseDTO(saveFlight);
    }

    @Override
    public FlightResponseDTO updateFlight(Long flightId, FlightRequestDTO flightRequestDto) {
        List<String> errors = FlightValidator.validateFlight(flightRequestDto);
        if(!errors.isEmpty()){
            log.error("Invalid flight data");
            throw new InvalidEntityException("Invalid flight data", errors);
        }

        Flight existingFlight = getExistingFlightId(flightId);
        updateExistingFlightRequestDto(existingFlight, flightRequestDto);
        Flight updateFlight = flightRepository.save(existingFlight);

        return flightMapper.flightToFlightResponseDTO(updateFlight);
    }

    private void updateExistingFlightRequestDto(Flight existingFlight, FlightRequestDTO flightRequestDto){
        existingFlight.setCityStart(flightRequestDto.getCityStart());
        existingFlight.setCityArrive(flightRequestDto.getCityArrive());
        existingFlight.setNumberOfPlace(flightRequestDto.getNumberOfPlace());
        existingFlight.setHourDateStart(flightRequestDto.getHourDateStart());
        existingFlight.setHourDateArrive(flightRequestDto.getHourDateArrive());
        existingFlight.setAirplane(flightRequestDto.getAirplane());
    }

    public Flight getExistingFlightId(Long flightId){
        if(flightId == null){
            log.error("Flight with Id is null");
            return null;
        }

        return flightRepository.findById(flightId).orElseThrow(()->
                new EntityNotFoundException(
                        "Flight with Id = " + flightId + " does not exist in DataBase"
                ));
    }

    @Override
    public List<FlightResponseDTO> getAllFlight() {
        List<Flight> flightList = flightRepository.findAll();

        return flightList.stream()
                .map(flight -> flightMapper.flightToFlightResponseDTO(flight))
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightResponseDTO> searchFlight(String cityStart, String cityArrive, String hourDateStart, String hourDateArrive) {
        if(cityStart.equals(cityArrive)){
            log.error("Cities don't have to be the same");
            throw new InvalidEntityException(
                    "Cities don't have to be the same - start = " + cityStart + " arrive = "+ cityArrive,
                    HttpStatus.NOT_ACCEPTABLE
            );
        }


          List<Flight> flightList = flightRepository.findFlightByCityStartAndCityArriveAndHourDateStartAndHourDateArrive(
                  cityStart, cityArrive, hourDateStart, hourDateArrive);

        return flightList.stream()
                .map(flight ->{
                    FlightResponseDTO flightResponseDTO = flightMapper.flightToFlightResponseDTO(flight);
                    flightResponseDTO.setAirplane(flight.getAirplane());
                    flightResponseDTO.setAirport(flight.getAirport());

                    return flightResponseDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public FlightResponseDTO getFlightId(Long flightId) {
        Flight existingFlight = getExistingFlightId(flightId);
        return flightMapper.flightToFlightResponseDTO(existingFlight);
    }

    @Override
    public void deleteFlight(Long flightId) {
        if(flightId == null){
            log.error("Flight with Id is null");
            return;
        }

        flightRepository.deleteById(flightId);
    }
}
