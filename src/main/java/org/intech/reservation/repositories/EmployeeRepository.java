package org.intech.reservation.repositories;

import org.intech.reservation.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}