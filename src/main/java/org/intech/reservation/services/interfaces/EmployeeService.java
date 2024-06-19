package org.intech.reservation.services.interfaces;

import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.intech.reservation.dtos.response.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO saveEmployee(EmployeeRequestDTO employeeRequestDto);
    EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeRequestDTO employeeRequestDto);
    List<EmployeeResponseDTO> getAllEmployee();
    EmployeeResponseDTO getEmployeeId(Long employeeId);
    void deleteEmployee(Long employeeId);
}
