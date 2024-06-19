package org.intech.reservation.validators;

import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EmployeeValidator {
    public static List<String> validateEmployee(EmployeeRequestDTO employeeRequestDto){
        List<String> errors = new ArrayList<>();

        if(employeeRequestDto == null) {
            errors.add("Can you enter your employee number");
            errors.addAll(UserValidator.validateUser(null));

            return errors;
        }

        if(employeeRequestDto.getNumEmp() == null){
            errors.add("Can you enter your employee number");
        }
        errors.addAll(UserValidator.validateUser(employeeRequestDto));

        return errors;
    }
}
