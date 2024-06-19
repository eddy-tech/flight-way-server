package org.intech.reservation.validators;

import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AirplaneValidator {
    public static List<String> validatePlane(AirplaneRequestDTO airplaneRequestDto) {
        List<String> errors = new ArrayList<>();

        if(airplaneRequestDto == null) {
            errors.add("Can you add plane's brand");
            errors.add("Can you add plane's model");
            errors.add("Can you add the year of manufacture of the airplane");

            return errors;
        }

        if(!StringUtils.hasLength(airplaneRequestDto.getBrand())) {
            errors.add("Can you add plane's brand");
        }
        if(!StringUtils.hasLength(airplaneRequestDto.getModel())) {
            errors.add("Can you add plane's model");
        }
        if(airplaneRequestDto.getYearMaking() == null) {
            errors.add("Can you add year of manufacture of the airplane");
        }
//        if(airplaneRequestDto.getYearMaking().toString().length() > 4){
//            errors.add("Can you write well year of manufacture of the airplane with 4 digits");
//        }

        return errors;
    }
}
