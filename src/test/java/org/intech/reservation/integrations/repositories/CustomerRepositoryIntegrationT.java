package org.intech.reservation.integrations.repositories;

import org.intech.reservation.entities.Customer;
import org.intech.reservation.integrations.containers.AbstractionContainerBaseTest;
import org.intech.reservation.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class CustomerRepositoryIntegrationT extends AbstractionContainerBaseTest {
    @Autowired
    private CustomerRepository customerRepository;
    Customer customer;

    @BeforeEach
    void setup(){
        customer = new Customer();
        customer.setNumPassport("B14856");
        customer.setFirstName("Le Marchand");
        customer.setLastName("Auguste");
        customer.setEmail("auguste@gmail.com");
        customer.setTelephone(75689856L);
        customer.setAddresses("10 rue de la joie");
        customer.setBirthdate("12-02-1998");
    }

    @Test
    void givenNumPassport_whenFindByNumPassport_thenReturnCustomerObject() {
        customerRepository.save(customer);

        Customer saveCustomer = customerRepository.findCustomerByNumPassport(customer.getNumPassport());

        assertThat(saveCustomer).isNotNull();
    }

    @Test
    void givenNumPassport_whenExistNumPassportDataBase_thenReturnBoolean() {
        customerRepository.save(customer);

        Boolean existingCustomer = customerRepository.existsCustomerByNumPassport(customer.getNumPassport());
        Boolean customerNotExists = customerRepository.existsCustomerByNumPassport("A125659");

        assertThat(existingCustomer).isTrue();
        assertThat(customerNotExists).isFalse();
    }

}
