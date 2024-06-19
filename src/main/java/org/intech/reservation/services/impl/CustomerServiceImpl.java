package org.intech.reservation.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.CustomerRequestDTO;
import org.intech.reservation.dtos.response.CustomerResponseDTO;
import org.intech.reservation.entities.Customer;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.CustomerMapper;
import org.intech.reservation.repositories.CustomerRepository;
import org.intech.reservation.services.interfaces.CustomerService;
import org.intech.reservation.validators.CustomerValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    @Override
    public CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDto) {
        List<String> errors = CustomerValidator.validateCustomer(customerRequestDto);
        if(!errors.isEmpty()){
            log.error("Customer is invalid", customerRequestDto);
            throw new InvalidEntityException("Customer is invalid", errors);
        }

        Boolean existingPassport = customerRepository.existsCustomerByNumPassport(customerRequestDto.getNumPassport());
        if(existingPassport){
            log.info("This customer already exists");
            throw new InvalidEntityException(
                    "Customer with num passport = " + customerRequestDto.getNumPassport() + " is already existing in database",
                    HttpStatus.FOUND
                    );
        }

        Customer customer = customerMapper.customerRequestDTOToCustomer(customerRequestDto);
        saveCustomerFromCustomerDto(customer, customerRequestDto);
        Customer saveCustomer = customerRepository.save(customer);

        return customerMapper.customerToCustomerResponseDTO(saveCustomer);
    }

    private void saveCustomerFromCustomerDto(Customer customer, CustomerRequestDTO customerRequestDto) {
        customer.setFirstName(customerRequestDto.getFirstName());
        customer.setLastName(customerRequestDto.getLastName());
        customer.setEmail(customerRequestDto.getEmail());
        customer.setAddresses(customerRequestDto.getAddresses());
        customer.setBirthdate(customerRequestDto.getBirthdate());
        customer.setTelephone(customerRequestDto.getTelephone());
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long userId, CustomerRequestDTO customerRequestDto) {
        List<String> errors = CustomerValidator.validateCustomer(customerRequestDto);
        if(!errors.isEmpty()){
            log.error("Invalid customer data", customerRequestDto);
            throw new InvalidEntityException("Invalid customer data",  errors);
        }

        Customer existingCustomer = getExistingCustomerId(userId);
        updateCustomerRequestDto(existingCustomer, customerRequestDto);
        Customer updateCustomer = customerRepository.save(existingCustomer);

        return customerMapper.customerToCustomerResponseDTO(updateCustomer);
    }

    private void updateCustomerRequestDto(Customer existingCustomer, CustomerRequestDTO customerRequestDto){
        existingCustomer.setNumPassport(customerRequestDto.getNumPassport());
        existingCustomer.setFirstName(customerRequestDto.getFirstName());
        existingCustomer.setLastName(customerRequestDto.getLastName());
        existingCustomer.setEmail(customerRequestDto.getEmail());
        existingCustomer.setAddresses(customerRequestDto.getAddresses());
        existingCustomer.setBirthdate(existingCustomer.getBirthdate());
        existingCustomer.setTelephone(existingCustomer.getTelephone());
    }

    public Customer getExistingCustomerId(Long userId){
        if(userId == null){
            log.error("Customer ID is null");
            return null;
        }

       return customerRepository.findById(userId).orElseThrow(()->
                new EntityNotFoundException(
                        "Customer with ID = " + userId + " does not exist in DataBase"
                ));
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();

        return customerList.stream()
                .map(customer -> customerMapper.customerToCustomerResponseDTO(customer))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerId(Long customerId) {
        Customer existingCustomer = getExistingCustomerId(customerId);
        return customerMapper.customerToCustomerResponseDTO(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        if(customerId == null){
            log.error("Customer ID is null");
            return;
        }
        customerRepository.deleteById(customerId);
    }
}
