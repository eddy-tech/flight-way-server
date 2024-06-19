package org.intech.reservation.validators;

import org.intech.reservation.dtos.request.AirportRequestDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AirportValidator {
    public static List<String> validateAirport(AirportRequestDTO airportRequestDto) {
        List<String> errors = new ArrayList<>();

        if(airportRequestDto == null){
            errors.add("Can you add Airport name");
            errors.add("Can you please add airport's country");
            errors.add("Can you please add airport's city");

            return errors;
        }

        if(!StringUtils.hasLength(airportRequestDto.getNameAirport())) {
            errors.add("Can you add Airport name");
        }
        if(!StringUtils.hasLength(airportRequestDto.getCountry())) {
            errors.add("Can you please add airport's country");
        }
        if(!StringUtils.hasLength(airportRequestDto.getCity())) {
            errors.add("Can you please add airport's city");
        }

        return errors;
    }
}
