package org.intech.reservation.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.AirportRequestDTO;
import org.intech.reservation.dtos.response.AirportResponseDTO;
import org.intech.reservation.entities.Airport;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.AirportMapper;
import org.intech.reservation.repositories.AirportRepository;
import org.intech.reservation.services.interfaces.AirportService;
import org.intech.reservation.validators.AirportValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AirportServiceImpl implements AirportService {
    private AirportRepository airportRepository;
    private AirportMapper airportMapper;

    @Override
    public AirportResponseDTO saveAirport(AirportRequestDTO airportRequestDto) {
        List<String> errors = AirportValidator.validateAirport(airportRequestDto);
        if(!errors.isEmpty()){
            log.error("Airport contains invalid data");
            throw new InvalidEntityException("Airport contains invalid data", errors);
        }

        Airport airport = airportMapper.airportRequestDTOToAirport(airportRequestDto);
        Airport saveAirport = airportRepository.save(airport);

        return airportMapper.airportToAirportResponseDTO(saveAirport);
    }

    @Override
    public AirportResponseDTO updateAirport(Long airportId, AirportRequestDTO airportRequestDto) {
        List<String> errors = AirportValidator.validateAirport(airportRequestDto);
        if(!errors.isEmpty()){
            log.error("Invalid airport data", airportRequestDto);
            throw new InvalidEntityException("Invalid Airport data", errors);
        }

        Airport existingAirport = getExistingAirportId(airportId);
        updateAirportFromRequestDto(existingAirport, airportRequestDto);
        Airport updateAirport = airportRepository.save(existingAirport);

        return airportMapper.airportToAirportResponseDTO(updateAirport);
    }

    private void updateAirportFromRequestDto(Airport existingAirport, AirportRequestDTO airportRequestDto){
        existingAirport.setNameAirport(airportRequestDto.getNameAirport());
        existingAirport.setCity(airportRequestDto.getCity());
        existingAirport.setCountry(airportRequestDto.getCountry());
    }

    public Airport getExistingAirportId(Long airportId){
        if(airportId == null){
            log.error("Airport ID is null");
            return null;
        }

        return airportRepository.findById(airportId).orElseThrow(()->
                new EntityNotFoundException(
                        "Airport with ID = " + airportId + " does not exist in DataBase"
                ));
    }

    @Override
    public List<AirportResponseDTO> getAllAirport() {
        List<Airport> airportList = airportRepository.findAll();

        return airportList.stream()
                .map(airport -> airportMapper.airportToAirportResponseDTO(airport))
                .collect(Collectors.toList());
    }

    @Override
    public AirportResponseDTO getAirportId(Long airportId) {
        Airport existingAirport = getExistingAirportId(airportId);
        return airportMapper.airportToAirportResponseDTO(existingAirport);
    }

    @Override
    public void deleteAirport(Long airportId) {
        if(airportId == null){
            log.error("Airport ID is null");
            return;
        }
        airportRepository.deleteById(airportId);
    }
}
