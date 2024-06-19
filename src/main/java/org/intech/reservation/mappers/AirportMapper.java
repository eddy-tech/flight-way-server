package org.intech.reservation.mappers;

import org.intech.reservation.dtos.request.AirportRequestDTO;
import org.intech.reservation.dtos.response.AirportResponseDTO;
import org.intech.reservation.entities.Airport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    AirportResponseDTO airportToAirportResponseDTO(Airport airport);
    Airport airportRequestDTOToAirport (AirportRequestDTO airportRequestDTO);
}
