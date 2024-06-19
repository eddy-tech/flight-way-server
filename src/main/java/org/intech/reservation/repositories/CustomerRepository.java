package org.intech.reservation.repositories;

import org.intech.reservation.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerByNumPassport(String numPassport);
    Boolean existsCustomerByNumPassport(String numPassport);
}