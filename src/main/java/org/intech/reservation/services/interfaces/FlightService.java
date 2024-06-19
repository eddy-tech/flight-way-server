package org.intech.reservation.services.interfaces;

import org.intech.reservation.dtos.request.FlightRequestDTO;
import org.intech.reservation.dtos.response.FlightResponseDTO;

import java.util.List;

public interface FlightService {
    FlightResponseDTO saveFlight(FlightRequestDTO flightRequestDto);
    FlightResponseDTO updateFlight(Long flightId, FlightRequestDTO flightRequestDto);
    List<FlightResponseDTO> getAllFlight();
    List<FlightResponseDTO> searchFlight(String cityStart, String cityArrive, String hourDateStart, String hourDateArrive);
    FlightResponseDTO getFlightId(Long flightId);
    void deleteFlight(Long flightId);
}
