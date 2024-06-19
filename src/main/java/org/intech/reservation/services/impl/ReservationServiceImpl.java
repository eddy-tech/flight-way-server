package org.intech.reservation.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.ReservationRequestDTO;
import org.intech.reservation.dtos.request.ReservationRequestNewCustomerDTO;
import org.intech.reservation.dtos.response.ReservationResponseDTO;
import org.intech.reservation.entities.Customer;
import org.intech.reservation.entities.Flight;
import org.intech.reservation.entities.Reservation;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.ReservationMapper;
import org.intech.reservation.repositories.CustomerRepository;
import org.intech.reservation.repositories.FlightRepository;
import org.intech.reservation.repositories.ReservationRepository;
import org.intech.reservation.services.interfaces.ReservationService;
import org.intech.reservation.validators.ReservationValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private CustomerRepository customerRepository;
    private FlightRepository flightRepository;
    private ReservationMapper reservationMapper;


    @Override
    public ReservationResponseDTO makeReservationForCustomerExists(ReservationRequestDTO reservationRequestDTO) {
        List<String> errors = ReservationValidator.validateReservation(reservationRequestDTO);
        if(!errors.isEmpty()){
            log.error("Reservation contains some errors");
            throw new InvalidEntityException("Your reservation contains some errors", HttpStatus.BAD_REQUEST, errors);
        }

        Boolean existingFlight = flightRepository.existingFlightNumber(reservationRequestDTO.getFlightNumber());
        if(!existingFlight){
            log.info("This flight does not exists");
            throw new InvalidEntityException(
                    "Flight with num flight = " + reservationRequestDTO.getFlightNumber() + " does not exists in database",
                    HttpStatus.NOT_FOUND
            );
        }

        Customer customerRes = customerRepository.findCustomerByNumPassport(reservationRequestDTO.getNumPassport());

        return createReservation(customerRes, getFlightByNumFlight(reservationRequestDTO));
    }

    @Override
    public ReservationResponseDTO makeReservationForNewCustomer(ReservationRequestNewCustomerDTO reservationRequestNewCustomerDTO) {
        List<String> errors = ReservationValidator.validateReservation(reservationRequestNewCustomerDTO);
        if(!errors.isEmpty()){
            log.error("Reservation contains some errors");
            throw new InvalidEntityException("Your reservation contains some errors", HttpStatus.BAD_REQUEST, errors);
        }

        Customer newCustomerReservation = createNewCustomer(reservationRequestNewCustomerDTO);

        return createReservation(newCustomerReservation, getFlightByNumFlight(reservationRequestNewCustomerDTO));
    }

    public Flight getFlightByNumFlight(ReservationRequestDTO reservationRequestDTO) {
        Boolean existsNumPassport = customerRepository.existsCustomerByNumPassport(reservationRequestDTO.getNumPassport());
        if(!existsNumPassport){
            log.info("This passport number does not exists");
            throw new InvalidEntityException(
                    "Customer with num passport = " + reservationRequestDTO.getNumPassport() + " does not exists in database",
                    HttpStatus.NOT_FOUND
            );
        }

        return flightRepository.findFlightByFlightNumber(reservationRequestDTO.getFlightNumber());
    }


    public ReservationResponseDTO createReservation(Customer customerReservation, Flight flightReservation){
        Reservation reservation = Reservation.builder()
                .bookedFlight(flightReservation)
                .bookedBy(customerReservation)
                .build();
        Reservation saveReservation = reservationRepository.save(reservation);

        return reservationMapper.reservationToReservationSTO(saveReservation);
    }

    public Customer createNewCustomer(ReservationRequestNewCustomerDTO reservationRequestDTO){
        Customer customerReservation = new Customer();
        customerReservation.setFirstName(reservationRequestDTO.getFirstName());
        customerReservation.setLastName(reservationRequestDTO.getLastName());
        customerReservation.setEmail(reservationRequestDTO.getEmail());
        customerReservation.setBirthdate(reservationRequestDTO.getBirthdate());
        customerReservation.setAddresses(reservationRequestDTO.getAddresses());
        customerReservation.setTelephone(reservationRequestDTO.getTelephone());
        customerReservation.setNumPassport(reservationRequestDTO.getNumPassport());

        return customerRepository.save(customerReservation);
    }
}
