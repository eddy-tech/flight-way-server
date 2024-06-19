package org.intech.reservation.validators;

import org.intech.reservation.dtos.request.UserRequestDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {
    public static List<String> validateUser(UserRequestDTO userRequestDto) {
         List<String> errors = new ArrayList<>();

        if(userRequestDto == null) {
            errors.add("Can you add your firstName");
            errors.add("Can you add your lastName");
            errors.add("Can you add your Email");
            errors.add("Can you add your birthday");
            errors.add("Can you add your address");
            errors.add("Can you add your number phone");

            return errors;
        }

        if(!StringUtils.hasLength(userRequestDto.getFirstName())) {
            errors.add("Can you add your firstName");
        }
        if(!StringUtils.hasLength(userRequestDto.getLastName())) {
            errors.add("Can you add your lastName");
        }
        if(!StringUtils.hasLength(userRequestDto.getEmail())) {
            errors.add("Can you add your Email");
        }
        if(!StringUtils.hasLength(userRequestDto.getBirthdate())) {
            errors.add("Can you add your birthday");
        }
        if(userRequestDto.getTelephone() == null) {
            errors.add("Can you add your number phone");
        }
        if(!StringUtils.hasLength(userRequestDto.getAddresses())) {
            errors.add("Can you add your address");
        }

        return errors;
    }
}
