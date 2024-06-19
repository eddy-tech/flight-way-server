package org.intech.reservation.services.interfaces;


import org.intech.reservation.dtos.request.ReservationRequestDTO;
import org.intech.reservation.dtos.request.ReservationRequestNewCustomerDTO;
import org.intech.reservation.dtos.response.ReservationResponseDTO;

public interface ReservationService {
    ReservationResponseDTO makeReservationForCustomerExists(ReservationRequestDTO reservationRequestDTO);
    ReservationResponseDTO makeReservationForNewCustomer(ReservationRequestNewCustomerDTO reservationRequestNewCustomerDTO);
}
