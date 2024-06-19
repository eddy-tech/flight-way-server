package org.intech.reservation.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AirplaneResponseDTO {
    private Long airplaneId;
    private String brand;
    private String model;
    private Long yearMaking;
}
