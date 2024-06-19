package org.intech.reservation.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestNewCustomerDTO extends ReservationRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String birthdate;
    private String addresses;
    private Long telephone;
}
