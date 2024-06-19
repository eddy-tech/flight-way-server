package org.intech.reservation.services.interfaces;


import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.response.AirplaneResponseDTO;

import java.util.List;

public interface AirplaneService {
    AirplaneResponseDTO savePlane(AirplaneRequestDTO airplaneRequestDto);
    AirplaneResponseDTO updatePlane(Long airplaneId, AirplaneRequestDTO airplaneRequestDto);
    List<AirplaneResponseDTO> getAllPlane();
    AirplaneResponseDTO getAirplaneId(Long airplaneId);
    void deletePlane(Long planeId);
}
