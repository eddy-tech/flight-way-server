package org.intech.reservation.web;

import lombok.AllArgsConstructor;
import org.intech.reservation.dtos.request.CustomerRequestDTO;
import org.intech.reservation.dtos.response.CustomerResponseDTO;
import org.intech.reservation.roots.CustomerEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(RootEndPoint.API_ROOT)
@CrossOrigin("*")
public class CustomerRestController {
    private CustomerService customerService;

    @PostMapping(CustomerEndPoint.CUSTOMER_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO saveCustomer(@RequestBody CustomerRequestDTO customerRequestDto) {
        return customerService.saveCustomer(customerRequestDto);
    }

    @PutMapping(CustomerEndPoint.CUSTOMER_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponseDTO updateCustomer(@PathVariable(name = "id") Long customerId, @RequestBody CustomerRequestDTO customerRequestDto) {
        return customerService.updateCustomer(customerId, customerRequestDto);
    }

    @GetMapping(CustomerEndPoint.CUSTOMER_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(CustomerEndPoint.CUSTOMER_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponseDTO getCustomerId(@PathVariable(name = "id") Long customerId) {
        return customerService.getCustomerId(customerId);
    }

    @DeleteMapping(CustomerEndPoint.CUSTOMER_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(@PathVariable(name = "id") Long customerId) {
        customerService.deleteCustomer(customerId);
    }
}
