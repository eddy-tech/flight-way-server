package org.intech.reservation.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intech.reservation.entities.Customer;
import org.intech.reservation.entities.Flight;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO{
    private Long reservationId;
    private Customer customer;
    private Flight flight;
}
