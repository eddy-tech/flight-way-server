package org.intech.reservation.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class AirportRequestDTO {
    private String nameAirport;
    private String country;
    private String city;
}