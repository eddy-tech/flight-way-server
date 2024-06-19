package org.intech.reservation.mappers;

import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.intech.reservation.dtos.response.EmployeeResponseDTO;
import org.intech.reservation.entities.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeResponseDTO employeeToEmployeeResponseDTO(Employee employee);
    Employee employeeRequestDTOToEmployee(EmployeeRequestDTO employeeRequestDTO);
}
