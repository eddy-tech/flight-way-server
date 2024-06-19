package org.intech.reservation.validators;

import org.intech.reservation.dtos.request.CustomerRequestDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerValidator {
    public static List<String> validateCustomer (CustomerRequestDTO customerRequestDTO){
        List<String> errors = new ArrayList<>();

        if(customerRequestDTO == null) {
            errors.add("Can you enter your passport number");
            errors.addAll(UserValidator.validateUser(null));
            return errors;
        }

        if(!StringUtils.hasLength(customerRequestDTO.getNumPassport())){
            errors.add("Can you enter your passport number");
        }

        errors.addAll(UserValidator.validateUser(customerRequestDTO));

        return errors;
    }
}
