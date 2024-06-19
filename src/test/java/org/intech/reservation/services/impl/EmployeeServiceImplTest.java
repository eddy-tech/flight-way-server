package org.intech.reservation.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.intech.reservation.dtos.response.EmployeeResponseDTO;
import org.intech.reservation.entities.Employee;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.EmployeeMapper;
import org.intech.reservation.repositories.EmployeeRepository;
import org.intech.reservation.validators.EmployeeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    EmployeeResponseDTO employeeResponseDTO;
    EmployeeRequestDTO employeeRequestDTO;
    EmployeeRequestDTO invalidRequest;
    List<String> expectedErrors;
    InvalidEntityException exception;
    Employee employee;
    List<String> errors;
    Long userId = 1L;
    String firstName = "Oumar";
    String lastName = "Sy";
    String birthday = "12-02-1998";
    Long phoneNumber = 756589956L;
    String address = "12 rue de la peace";
    Long numEmp = 789563L;
    String email = "oumar@gmail.com";


    @BeforeEach
    void setUp() {
        employeeResponseDTO = new EmployeeResponseDTO();
        employeeResponseDTO.setFirstName(firstName);
        employeeResponseDTO.setLastName(lastName);
        employeeResponseDTO.setNumEmp(numEmp);
        employeeResponseDTO.setEmail(email);
        employeeResponseDTO.setTelephone(phoneNumber);
        employeeResponseDTO.setBirthdate(birthday);
        employeeResponseDTO.setAddresses(address);
        employeeResponseDTO.setUserId(userId);

        employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFirstName(firstName);
        employeeRequestDTO.setLastName(lastName);
        employeeRequestDTO.setNumEmp(numEmp);
        employeeRequestDTO.setEmail(email);
        employeeRequestDTO.setTelephone(phoneNumber);
        employeeRequestDTO.setBirthdate(birthday);
        employeeRequestDTO.setAddresses(address);

        employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setNumEmp(numEmp);
        employee.setEmail(email);
        employee.setTelephone(phoneNumber);
        employee.setBirthdate(birthday);
        employee.setAddresses(address);
        employee.setId(userId);

        invalidRequest = new EmployeeRequestDTO();
        invalidRequest.setFirstName("");
        invalidRequest.setLastName("");
        invalidRequest.setNumEmp(null);
        invalidRequest.setEmail("");
        invalidRequest.setTelephone(null);
        invalidRequest.setBirthdate("");
        invalidRequest.setAddresses("");

        expectedErrors = Arrays.asList(
                "Can you enter your employee number",
                "Can you add your firstName",
                "Can you add your lastName",
                "Can you add your Email",
                "Can you add your birthday",
                "Can you add your address",
                "Can you add your number phone"
        );
    }

    @Test
    void givenEmployeeRequestDTO_whenSaveEmployee_thenReturnEmployeeResponseDTO() {
        errors = EmployeeValidator.validateEmployee(employeeRequestDTO);
        when(employeeMapper.employeeRequestDTOToEmployee(employeeRequestDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.employeeToEmployeeResponseDTO(employee)).thenReturn(employeeResponseDTO);

        EmployeeResponseDTO saveEmployee = employeeService.saveEmployee(employeeRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(errors).hasSize(0);
        assertThat(saveEmployee).isNotNull();
    }

    @Test
    void givenInvalidEmployeeRequestDTO_whenValidateEmployee_thenReturnErrors(){
        errors = EmployeeValidator.validateEmployee(invalidRequest);

        assertThat(errors).hasSize(7);
        assertThat(errors).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenInvalidEmployeeRequestDTO_whenSaveEmployee_thenThrowsInvalidEntityException(){
        exception = assertThrows(InvalidEntityException.class, () -> {
            employeeService.saveEmployee(invalidRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Employee is invalid");
        assertThat(exception.getErrors()).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenEmployeeRequestDTO_whenUpdateEmployee_thenReturnEmployeeResponseDTO() {
        employeeRequestDTO.setTelephone(658958568L);
        employeeRequestDTO.setEmail("oumarsy@gmail.com");
        employeeResponseDTO.setTelephone(658958568L);
        employeeResponseDTO.setEmail("oumarsy@gmail.com");
        employee.setTelephone(658958568L);
        employee.setEmail("oumarsy@gmail.com");

        errors = EmployeeValidator.validateEmployee(employeeRequestDTO);
        given(employeeRepository.findById(userId)).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);

        given(employeeMapper.employeeToEmployeeResponseDTO(employee)).willReturn(employeeResponseDTO);

        EmployeeResponseDTO updateEmployee = employeeService.updateEmployee(userId, employeeRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(updateEmployee.getTelephone()).isNotEqualTo(phoneNumber);
        assertThat(updateEmployee.getEmail()).isNotEqualTo(email);
        assertThat(updateEmployee.getTelephone()).isEqualTo(658958568L);
        assertThat(updateEmployee.getEmail()).isEqualTo("oumarsy@gmail.com");
    }

    @Test
    void givenEmployeeId_whenGetExistingEmployeeId_thenReturnEmployeeResponseDTO() {
        given(employeeRepository.findById(userId)).willReturn(Optional.of(employee));

        Employee result = employeeService.getExistingEmployeeId(userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo(firstName);
        assertThat(result.getLastName()).isEqualTo(lastName);
        assertThat(result.getNumEmp()).isEqualTo(numEmp);
        assertThat(result.getBirthdate()).isEqualTo(birthday);
        assertThat(result.getAddresses()).isEqualTo(address);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getTelephone()).isEqualTo(phoneNumber);
    }

    @Test
    void givenEmployeeList_whenGetAllEmployee_thenReturnEmployeeResponseList() {
        given(employeeRepository.findAll()).willReturn(List.of(employee));

        List<EmployeeResponseDTO> customerList = employeeService.getAllEmployee();
        log.info(customerList.toString());

        assertThat(customerList).isNotNull();
        assertThat(customerList.size()).isEqualTo(1);
    }

    @Test
    void givenEmployeeRequestDTO_whenGetAllEmployee_thenReturnEmployeeResponseDTOList() {
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        List<EmployeeResponseDTO> employeeList = employeeService.getAllEmployee();

        assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    void givenEmployeeRequestDTO_GetEmployeeId_thenReturnEmployeeResponseDTO() {
        given(employeeRepository.findById(userId)).willReturn(Optional.of(employee));
        given(employeeMapper.employeeToEmployeeResponseDTO(employee)).willReturn(employeeResponseDTO);

        EmployeeResponseDTO saveEmployee = employeeService.getEmployeeId(employee.getId());

        assertThat(saveEmployee).isNotNull();
        assertThat(saveEmployee.getUserId()).isEqualTo(1L);
        assertThat(saveEmployee.getFirstName()).isEqualTo(firstName);
        assertThat(saveEmployee.getLastName()).isEqualTo(lastName);
        assertThat(saveEmployee.getEmail()).isEqualTo(email);
        assertThat(saveEmployee.getAddresses()).isEqualTo(address);
        assertThat(saveEmployee.getTelephone()).isEqualTo(phoneNumber);
        assertThat(saveEmployee.getBirthdate()).isEqualTo(birthday);
        assertThat(saveEmployee.getNumEmp()).isEqualTo(numEmp);

        verify(employeeMapper, times(1)).employeeToEmployeeResponseDTO(employee);
    }

    @Test
    void givenNullEmployeeId_whenGetExistingEmployeeById_thenThrowEntityNotFoundException() {
        Long employeeWrongId = 10L;
        given(employeeRepository.findById(employeeWrongId)).willReturn(Optional.empty());

        assertThatThrownBy(()-> employeeService.getExistingEmployeeId(employeeWrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee with ID = " + employeeWrongId + " does not exist in DataBase");
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        willDoNothing().given(employeeRepository).deleteById(userId);

        employeeService.deleteEmployee(userId);

        verify(employeeRepository, times(1)).deleteById(userId);
    }

}