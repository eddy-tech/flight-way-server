package org.intech.reservation.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AirplaneRequestDTO {
    private String brand;
    private String model;
    private Long yearMaking;
}
