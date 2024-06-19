package org.intech.reservation.web;

import lombok.AllArgsConstructor;
import org.intech.reservation.dtos.request.ReservationRequestDTO;
import org.intech.reservation.dtos.request.ReservationRequestNewCustomerDTO;
import org.intech.reservation.dtos.response.ReservationResponseDTO;
import org.intech.reservation.roots.ReservationEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping(RootEndPoint.API_ROOT)
@CrossOrigin("*")
public class ReservationRestController {
    private ReservationService reservationService;

    @PostMapping(ReservationEndPoint.CUSTOM_RESERVATION_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDTO makeReservationForNewCustomer(@RequestBody ReservationRequestNewCustomerDTO reservationRequestNewCustomerDTO) {
        return reservationService.makeReservationForNewCustomer(reservationRequestNewCustomerDTO);
    }

    @PostMapping(ReservationEndPoint.RESERVATION_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDTO makeReservationForCustomerExists(@RequestBody ReservationRequestDTO reservationRequestDTO) {
        return reservationService.makeReservationForCustomerExists(reservationRequestDTO);
    }
}
