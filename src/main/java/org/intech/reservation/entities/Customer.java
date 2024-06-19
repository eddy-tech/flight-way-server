package org.intech.reservation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor @NoArgsConstructor
@Builder
@Table(name = "customers")
public class Customer extends User {
    @Column(nullable = false, unique = true)
    private String numPassport;
}
