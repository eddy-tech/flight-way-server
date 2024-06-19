package org.intech.reservation.mappers;

import org.intech.reservation.dtos.request.ReservationRequestDTO;
import org.intech.reservation.dtos.response.ReservationResponseDTO;
import org.intech.reservation.entities.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    Reservation reservationRequestDTOToReservation (ReservationRequestDTO reservationRequestDTO);
    ReservationResponseDTO reservationToReservationSTO (Reservation reservation);
}
