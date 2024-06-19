package org.intech.reservation.mappers;

import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.response.AirplaneResponseDTO;
import org.intech.reservation.entities.Airplane;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirplaneMapper {
    AirplaneResponseDTO airplaneToPlaneResponseDTO (Airplane airplane);
    Airplane airplaneRequestDTOToAirplane (AirplaneRequestDTO airplaneRequestDTO);
}
