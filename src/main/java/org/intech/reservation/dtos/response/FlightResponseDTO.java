package org.intech.reservation.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intech.reservation.entities.Airplane;
import org.intech.reservation.entities.Airport;


@Data
@AllArgsConstructor @NoArgsConstructor
public class FlightResponseDTO {
    private Long flightId;
    private String flightNumber;
    private String cityStart;
    private String cityArrive;
    private Long numberOfPlace;
    private String hourDateStart;
    private String hourDateArrive;
    private Airplane airplane;
    private Airport airport;
}
