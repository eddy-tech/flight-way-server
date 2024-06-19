package org.intech.reservation.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.intech.reservation.dtos.response.EmployeeResponseDTO;
import org.intech.reservation.entities.Customer;
import org.intech.reservation.entities.Employee;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.EmployeeMapper;
import org.intech.reservation.repositories.EmployeeRepository;
import org.intech.reservation.services.interfaces.EmployeeService;
import org.intech.reservation.validators.CustomerValidator;
import org.intech.reservation.validators.EmployeeValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO employeeRequestDto) {
        List<String> errors = EmployeeValidator.validateEmployee(employeeRequestDto);
        if(!errors.isEmpty()){
            log.error("Employee is invalid", employeeRequestDto);
            throw new InvalidEntityException("Employee is invalid", errors);
        }

        Employee employee = employeeMapper.employeeRequestDTOToEmployee(employeeRequestDto);
        employee.setFirstName(employeeRequestDto.getFirstName());
        employee.setLastName(employeeRequestDto.getLastName());
        employee.setEmail(employeeRequestDto.getEmail());
        employee.setAddresses(employeeRequestDto.getAddresses());
        employee.setBirthdate(employeeRequestDto.getBirthdate());
        employee.setTelephone(employeeRequestDto.getTelephone());
        Employee saveEmployee = employeeRepository.save(employee);

        return employeeMapper.employeeToEmployeeResponseDTO(saveEmployee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeRequestDTO employeeRequestDto) {
        List<String> errors = EmployeeValidator.validateEmployee(employeeRequestDto);
        if(!errors.isEmpty()){
            log.error("Employee is invalid", employeeRequestDto);
            throw new InvalidEntityException("Employee is invalid", errors);
        }

        Employee existingEmployee = getExistingEmployeeId(employeeId);
        updateEmployeeFromEmployeeRequestDto(existingEmployee, employeeRequestDto);
        Employee updateEmployee = employeeRepository.save(existingEmployee);

        return employeeMapper.employeeToEmployeeResponseDTO(updateEmployee);
    }

    private void updateEmployeeFromEmployeeRequestDto(Employee existingEmployee, EmployeeRequestDTO employeeRequestDto){
        existingEmployee.setNumEmp(employeeRequestDto.getNumEmp());
        existingEmployee.setFirstName(employeeRequestDto.getFirstName());
        existingEmployee.setLastName(employeeRequestDto.getLastName());
        existingEmployee.setEmail(employeeRequestDto.getEmail());
        existingEmployee.setAddresses(employeeRequestDto.getAddresses());
        existingEmployee.setBirthdate(employeeRequestDto.getBirthdate());
        existingEmployee.setTelephone(employeeRequestDto.getTelephone());
    }

    public Employee getExistingEmployeeId(Long employeeId){
        if(employeeId == null){
            log.error("Employee ID is null");
            return null;
        }

        return employeeRepository.findById(employeeId).orElseThrow(()->
                new EntityNotFoundException(
                        "Employee with ID = " + employeeId + " does not exist in DataBase"
                ));
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();

        return employeeList.stream()
                .map(employee -> employeeMapper.employeeToEmployeeResponseDTO(employee))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getEmployeeId(Long employeeId) {
        Employee existingEmployee = getExistingEmployeeId(employeeId);
        return employeeMapper.employeeToEmployeeResponseDTO(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if(employeeId == null){
            log.error("Employee ID is null");
            return;
        }
        employeeRepository.deleteById(employeeId);
    }
}
