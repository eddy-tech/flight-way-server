package org.intech.reservation.services.interfaces;

import org.intech.reservation.dtos.request.CustomerRequestDTO;
import org.intech.reservation.dtos.response.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDto);
    CustomerResponseDTO updateCustomer(Long customerId, CustomerRequestDTO customerRequestDto);
    List<CustomerResponseDTO> getAllCustomers();
    CustomerResponseDTO getCustomerId(Long customerId);
    void deleteCustomer(Long customerId);
}
