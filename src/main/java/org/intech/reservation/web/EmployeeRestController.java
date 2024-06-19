package org.intech.reservation.web;

import lombok.AllArgsConstructor;
import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.intech.reservation.dtos.response.EmployeeResponseDTO;
import org.intech.reservation.roots.EmployeeEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(RootEndPoint.API_ROOT)
@CrossOrigin("*")
public class EmployeeRestController {
    private EmployeeService employeeService;

    @PostMapping(EmployeeEndPoint.EMPLOYEE_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('flight_admin')")
    public EmployeeResponseDTO saveCustomer(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.saveEmployee(employeeRequestDTO);
    }

    @PutMapping(EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public EmployeeResponseDTO updateCustomer(@PathVariable(name = "id") Long customerId, @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.updateEmployee(customerId, employeeRequestDTO);
    }

    @GetMapping(EmployeeEndPoint.EMPLOYEE_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public List<EmployeeResponseDTO> getAllCustomers() {
        return employeeService.getAllEmployee();
    }

    @GetMapping(EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public EmployeeResponseDTO getCustomerId(@PathVariable(name = "id") Long customerId) {
        return employeeService.getEmployeeId(customerId);
    }

    @DeleteMapping(EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId) {
        employeeService.deleteEmployee(customerId);
    }
}
