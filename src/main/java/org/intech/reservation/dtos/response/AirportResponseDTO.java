package org.intech.reservation.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class AirportResponseDTO {
    private Long airportId;
    private String nameAirport;
    private String country;
    private String city;
}
