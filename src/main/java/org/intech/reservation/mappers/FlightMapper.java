package org.intech.reservation.mappers;

import org.intech.reservation.dtos.request.FlightRequestDTO;
import org.intech.reservation.dtos.response.FlightResponseDTO;
import org.intech.reservation.entities.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    FlightResponseDTO flightToFlightResponseDTO (Flight flight);
    Flight flightRequestDTOToFlight(FlightRequestDTO flightRequestDto);
}
