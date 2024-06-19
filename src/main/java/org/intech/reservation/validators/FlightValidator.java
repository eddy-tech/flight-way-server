package org.intech.reservation.validators;

import org.intech.reservation.dtos.request.FlightRequestDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FlightValidator {
    public static List<String> validateFlight(FlightRequestDTO flightRequestDto) {
        List<String> errors = new ArrayList<>();

        if(flightRequestDto == null){
            errors.add("Can you add departure city");
            errors.add("Can you add city of arrival");
            errors.add("Can you add number of place");
            errors.add("Can you add hour of departure date");
            errors.add("Can you add hour of arrival date");

            return errors;
        }

        if(!StringUtils.hasLength(flightRequestDto.getCityStart())){
            errors.add("Can you add departure city");
        }
        if(!StringUtils.hasLength(flightRequestDto.getCityArrive())){
            errors.add("Can you add city of arrival");
        }
        if(flightRequestDto.getNumberOfPlace() == null){
            errors.add("Can you add number of place");
        }
        if(!StringUtils.hasLength(flightRequestDto.getHourDateStart())){
            errors.add("Can you add hour of departure date");
        }
        if(!StringUtils.hasLength(flightRequestDto.getHourDateArrive())){
            errors.add("Can you add hour of arrival date");
        }

        return errors;
    }
}
