package org.intech.reservation.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.CustomerRequestDTO;
import org.intech.reservation.dtos.response.CustomerResponseDTO;
import org.intech.reservation.entities.Customer;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.CustomerMapper;
import org.intech.reservation.repositories.CustomerRepository;
import org.intech.reservation.validators.CustomerValidator;
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
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;
    CustomerResponseDTO customerResponseDTO;
    CustomerRequestDTO customerRequestDTO;
    CustomerRequestDTO invalidRequest;
    List<String> expectedErrors;
    InvalidEntityException exception;
    Customer customer;
    List<String> errors;
    Long customerId = 1L;
    Long userId = 1L;
    String firstName = "Oumar";
    String lastName = "Sy";
    String birthday = "12-02-1998";
    Long phoneNumber = 756589956L;
    String address = "12 rue de la peace";
    String numPassport = "G12568";
    String email = "oumar@gmail.com";


    @BeforeEach
    void setUp() {
        customerResponseDTO = new CustomerResponseDTO();
            customerResponseDTO.setFirstName(firstName);
            customerResponseDTO.setLastName(lastName);
            customerResponseDTO.setNumPassport(numPassport);
            customerResponseDTO.setEmail(email);
            customerResponseDTO.setTelephone(phoneNumber);
            customerResponseDTO.setBirthdate(birthday);
            customerResponseDTO.setAddresses(address);
            customerResponseDTO.setUserId(userId);

        customerRequestDTO = new CustomerRequestDTO();
            customerRequestDTO.setFirstName(firstName);
            customerRequestDTO.setLastName(lastName);
            customerRequestDTO.setNumPassport(numPassport);
            customerRequestDTO.setEmail(email);
            customerRequestDTO.setTelephone(phoneNumber);
            customerRequestDTO.setBirthdate(birthday);
            customerRequestDTO.setAddresses(address);
            customerResponseDTO.setUserId(userId);

        customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setNumPassport(numPassport);
            customer.setEmail(email);
            customer.setTelephone(phoneNumber);
            customer.setBirthdate(birthday);
            customer.setAddresses(address);
            customer.setId(userId);

        invalidRequest = new CustomerRequestDTO();
        invalidRequest.setFirstName("");
        invalidRequest.setLastName("");
        invalidRequest.setNumPassport("");
        invalidRequest.setEmail("");
        invalidRequest.setTelephone(null);
        invalidRequest.setBirthdate("");
        invalidRequest.setAddresses("");

        expectedErrors = Arrays.asList(
                "Can you enter your passport number",
                "Can you add your firstName",
                "Can you add your lastName",
                "Can you add your Email",
                "Can you add your birthday",
                "Can you add your address",
                "Can you add your number phone"
        );
    }

    @Test
    void givenCustomerRequestDTO_whenSaveCustomer_thenReturnCustomerResponseDTO() {
        errors = CustomerValidator.validateCustomer(customerRequestDTO);
        when(customerMapper.customerRequestDTOToCustomer(customerRequestDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.customerToCustomerResponseDTO(customer)).thenReturn(customerResponseDTO);

        CustomerResponseDTO saveCustomer = customerService.saveCustomer(customerRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(errors).hasSize(0);
        assertThat(saveCustomer).isNotNull();
    }

    @Test
    void givenInvalidCustomerRequestDTO_whenValidateCustomer_thenReturnErrors(){
        errors = CustomerValidator.validateCustomer(invalidRequest);

        assertThat(errors).hasSize(7);
        assertThat(errors).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenInvalidCustomerRequestDTO_whenSaveCustomer_thenThrowsInvalidEntityException(){
        exception = assertThrows(InvalidEntityException.class, () -> {
            customerService.saveCustomer(invalidRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Customer is invalid");
        assertThat(exception.getErrors()).containsExactlyInAnyOrderElementsOf(expectedErrors);
    }

    @Test
    void givenCustomerRequestDTO_whenUpdateCustomer_thenReturnCustomerResponseDTO() {
        customerRequestDTO.setTelephone(658958568L);
        customerRequestDTO.setEmail("oumarsy@gmail.com");
        customerResponseDTO.setTelephone(658958568L);
        customerResponseDTO.setEmail("oumarsy@gmail.com");
        customer.setTelephone(658958568L);
        customer.setEmail("oumarsy@gmail.com");

        errors = CustomerValidator.validateCustomer(customerRequestDTO);
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));
        given(customerRepository.save(customer)).willReturn(customer);

        given(customerMapper.customerToCustomerResponseDTO(customer)).willReturn(customerResponseDTO);

        CustomerResponseDTO updateCustomer = customerService.updateCustomer(customerId, customerRequestDTO);

        assertThat(errors).isEmpty();
        assertThat(updateCustomer.getTelephone()).isNotEqualTo(phoneNumber);
        assertThat(updateCustomer.getEmail()).isNotEqualTo(email);
        assertThat(updateCustomer.getTelephone()).isEqualTo(658958568L);
        assertThat(updateCustomer.getEmail()).isEqualTo("oumarsy@gmail.com");
    }

    @Test
    void givenCustomerId_whenGetExistingCustomerId_thenReturnCustomerResponseDTO() {
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));

        Customer result = customerService.getExistingCustomerId(customerId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo(firstName);
        assertThat(result.getLastName()).isEqualTo(lastName);
        assertThat(result.getNumPassport()).isEqualTo(numPassport);
        assertThat(result.getBirthdate()).isEqualTo(birthday);
        assertThat(result.getAddresses()).isEqualTo(address);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getTelephone()).isEqualTo(phoneNumber);
    }

    @Test
    void givenCustomerList_whenGetAllCustomer_thenReturnCustomerResponseList() {
        given(customerRepository.findAll()).willReturn(List.of(customer));

        List<CustomerResponseDTO> customerList = customerService.getAllCustomers();
        log.info(customerList.toString());

        assertThat(customerList).isNotNull();
        assertThat(customerList.size()).isEqualTo(1);
    }

    @Test
    void givenCustomerRequestDTO_whenGetAllCustomers_thenReturnCustomerResponseDTOList() {
        given(customerRepository.findAll()).willReturn(Collections.emptyList());

        List<CustomerResponseDTO> customerList = customerService.getAllCustomers();

        assertThat(customerList.size()).isEqualTo(0);
    }

    @Test
    void givenCustomerRequestDTO_GetCustomerId_thenReturnCustomerResponseDTO() {
        given(customerRepository.findById(customerId)).willReturn(Optional.of(customer));
        given(customerMapper.customerToCustomerResponseDTO(customer)).willReturn(customerResponseDTO);

        CustomerResponseDTO saveCustomer = customerService.getCustomerId(customer.getId());

        assertThat(saveCustomer).isNotNull();
        assertThat(saveCustomer.getUserId()).isEqualTo(1L);
        assertThat(saveCustomer.getFirstName()).isEqualTo(firstName);
        assertThat(saveCustomer.getLastName()).isEqualTo(lastName);
        assertThat(saveCustomer.getEmail()).isEqualTo(email);
        assertThat(saveCustomer.getAddresses()).isEqualTo(address);
        assertThat(saveCustomer.getTelephone()).isEqualTo(phoneNumber);
        assertThat(saveCustomer.getBirthdate()).isEqualTo(birthday);
        assertThat(saveCustomer.getNumPassport()).isEqualTo(numPassport);

        verify(customerMapper, times(1)).customerToCustomerResponseDTO(customer);
    }

    @Test
    void givenNullCustomerId_whenGetExistingCustomerById_thenThrowEntityNotFoundException() {
        Long airplaneWrongId = 10L;
        given(customerRepository.findById(airplaneWrongId)).willReturn(Optional.empty());

        assertThatThrownBy(()-> customerService.getExistingCustomerId(airplaneWrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer with ID = " + airplaneWrongId + " does not exist in DataBase");
    }

    @Test
    void givenCustomerId_whenDeleteCustomer_thenReturnNothing() {
        willDoNothing().given(customerRepository).deleteById(customerId);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }
}