package org.intech.reservation.validators;

import org.intech.reservation.dtos.request.ReservationRequestDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReservationValidator {
    public static List<String> validateReservation(ReservationRequestDTO reservationRequestDTO){
        List<String> errors = new ArrayList<>();
        if(reservationRequestDTO == null) {
            errors.add("Can you please enter your firstName");
            errors.add("Can you please enter your lastName");
            errors.add("Can you please enter your passport number");
            errors.add("Can you please enter your birthday");
            errors.add("Can you please enter your departure's city");
            errors.add("Can you please enter your arrival's city");
            errors.add("Can you please enter your date time departure");
            errors.add("Can you please enter your date time arrival");

            return errors;
        }
//
//        if(CustomerValidator.validateCustomer(reservationRequestDTO.getCustomerDTO()).isEmpty()){
//            if(!StringUtils.hasLength(reservationRequestDTO.getFirstName())){
//                errors.add("Can you please enter your firstName");
//            }
//            if(!StringUtils.hasLength(reservationRequestDTO.getLastName())){
//                errors.add("Can you please enter your lastName");
//            }
//            if(reservationRequestDTO.getNumPassport() == null){
//                errors.add("Can you please enter your passport number");
//            }
//            if(!StringUtils.hasLength(reservationRequestDTO.getBirthDate())){
//                errors.add("Can you please enter your birthday");
//            }
//            if(!StringUtils.hasLength(reservationRequestDTO.getDepartureCity())){
//                errors.add("Can you please enter your departure's city");
//            }
//            if(!StringUtils.hasLength(reservationRequestDTO.getArrivalCity())){
//                errors.add("Can you please enter your arrival's city");
//            }
//            if(!StringUtils.hasLength(reservationRequestDTO.getDepartureDateTime())){
//                errors.add("Can you please enter your date time departure");
//            }
//            if(!StringUtils.hasLength(reservationRequestDTO.getArrivalDateTime())){
//                errors.add("Can you please enter your date time arrival");
//            }
//        }

        return errors;
    }
}