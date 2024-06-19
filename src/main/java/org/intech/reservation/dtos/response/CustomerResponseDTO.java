package org.intech.reservation.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CustomerResponseDTO extends UserResponseDTO {
    private String numPassport;
}
