package org.intech.reservation.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.intech.reservation.entities.Airplane;


@Data
@AllArgsConstructor @NoArgsConstructor
public class FlightRequestDTO {
    private String cityStart;
    private String cityArrive;
    private Long numberOfPlace;
    private String hourDateStart;
    private String hourDateArrive;
    Airplane airplane;
}
