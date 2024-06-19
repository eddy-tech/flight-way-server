package org.intech.reservation.services.interfaces;


import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.request.AirportRequestDTO;
import org.intech.reservation.dtos.response.AirportResponseDTO;

import java.util.List;

public interface AirportService {
    AirportResponseDTO saveAirport(AirportRequestDTO airportRequestDto);
    AirportResponseDTO updateAirport(Long airportId, AirportRequestDTO airportRequestDto);
    List<AirportResponseDTO> getAllAirport();
    AirportResponseDTO getAirportId(Long airportId);
    void deleteAirport(Long airportId);
}
