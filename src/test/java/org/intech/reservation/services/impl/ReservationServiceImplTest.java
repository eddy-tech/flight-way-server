package org.intech.reservation.services.impl;

import org.intech.reservation.dtos.request.ReservationRequestDTO;
import org.intech.reservation.dtos.request.ReservationRequestNewCustomerDTO;
import org.intech.reservation.dtos.response.ReservationResponseDTO;
import org.intech.reservation.entities.*;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.ReservationMapper;
import org.intech.reservation.repositories.CustomerRepository;
import org.intech.reservation.repositories.FlightRepository;
import org.intech.reservation.repositories.ReservationRepository;
import org.intech.reservation.services.interfaces.ReservationService;
import org.intech.reservation.validators.ReservationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private ReservationMapper reservationMapper;
    @InjectMocks
    private ReservationServiceImpl reservationService;
    ReservationResponseDTO reservationResponseDTO;
    ReservationRequestDTO reservationRequestDTO;
    ReservationRequestDTO invalidRequest;
    Reservation reservation;
    InvalidEntityException exception;
    ReservationRequestNewCustomerDTO reservationRequestNewCustomerDTO;
    ReservationRequestNewCustomerDTO invalidReservationNewCustomer;
    ReservationRequestNewCustomerDTO invalidNewCustomer;
    Flight flight;
    Customer customer;
    Airplane airplane;
    Airport airport;
    List<String> expectedErrors;
    Long userId = 1L;
    String firstName = "Oumar";
    String lastName = "Sy";
    String birthday = "12-02-1998";
    Long phoneNumber = 756589956L;
    String address = "12 rue de la peace";
    String email = "oumar@gmail.com";

    Long flightId = 1L;
    String cityStart = "Paris";
    String cityArrive = "Douala";
    Long numberOfPlace = 350L;
    String hourDateStart = "23:30:00";
    String hourDateArrive = "07:15:00";
    String flightNumber = "BTH458L90";

    Long airplaneId = 1L;
    String model = "AIR FRANCE";
    String brand = "A750";
    Long yearMaking = 1990L;

    Long airportId = 1L;
    String nameAirport = "Charles de Gaulle";
    String nameCity = "Paris";
    String country = "France";
    Long reservationId = 1L;
    String numPassport = "H15250";
    List<String> errors;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .id(reservationId)
                .bookedBy(customer)
                .bookedFlight(flight)
                .build();

        airplane = Airplane.builder()
                .id(airplaneId)
                .brand(brand)
                .model(model)
                .yearMaking(yearMaking)
                .build();

        airport = Airport.builder()
                .id(airportId)
                .nameAirport(nameAirport)
                .city(nameCity)
                .country(country)
                .build();

        customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setNumPassport(numPassport);
        customer.setBirthdate(birthday);
        customer.setTelephone(phoneNumber);
        customer.setAddresses(address);
        customer.setId(userId);

        flight = Flight.builder()
                .id(flightId)
                .flightNumber(flightNumber)
                .cityStart(cityStart)
                .cityArrive(cityArrive)
                .hourDateStart(hourDateStart)
                .hourDateArrive(hourDateArrive)
                .numberOfPlace(numberOfPlace)
                .airplane(airplane)
                .airport(airport)
                .build();

        reservationRequestDTO = new ReservationRequestDTO();
        reservationRequestDTO.setFlightNumber(flightNumber);
        reservationRequestDTO.setNumPassport(numPassport);

        reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setReservationId(reservationId);
        reservationResponseDTO.setCustomer(customer);
        reservationResponseDTO.setFlight(flight);

        reservationRequestNewCustomerDTO = new ReservationRequestNewCustomerDTO();
        reservationRequestNewCustomerDTO.setFirstName(firstName);
        reservationRequestNewCustomerDTO.setLastName(lastName);
        reservationRequestNewCustomerDTO.setEmail(email);
        reservationRequestNewCustomerDTO.setTelephone(phoneNumber);
        reservationRequestNewCustomerDTO.setBirthdate(birthday);
        reservationRequestNewCustomerDTO.setAddresses(address);
        reservationRequestNewCustomerDTO.setFlightNumber(flightNumber);
        reservationRequestNewCustomerDTO.setNumPassport(numPassport);

        invalidRequest = new ReservationRequestDTO();
        invalidRequest.setNumPassport("J58T150");

//        invalidNewCustomer = new ReservationRequestNewCustomerDTO();
//        invalidNewCustomer.setFirstName("");
//        invalidNewCustomer.setLastName("");
//        invalidNewCustomer.setBirthdate("");
//        invalidNewCustomer.setTelephone(null);
//        invalidNewCustomer.setAddresses("");
//        invalidNewCustomer.setEmail("");
//        invalidNewCustomer.setNumPassport("");

        expectedErrors = Arrays.asList(
        "Can you please enter your firstName",
        "Can you please enter your lastName",
        "Can you please enter your passport number",
        "Can you please enter your birthday",
        "Can you please enter your departure's city",
        "Can you please enter your arrival's city",
        "Can you please enter your date time departure",
        "Can you please enter your date time arrival"
        );
    }


    @Test
    @Disabled
    void givenReservationRequestDTO_whenMakeReservationForCustomerExists_thenReturnReservationResponseDTO() {
        when(ReservationValidator.validateReservation(reservationRequestDTO))
                .thenReturn(errors);
        when(flightRepository.existingFlightNumber(reservationRequestDTO.getFlightNumber()))
                .thenReturn(true);
        when(customerRepository.findCustomerByNumPassport(reservationRequestDTO.getNumPassport()))
                .thenReturn(customer);
        when(flightRepository.findFlightByFlightNumber(flight.getFlightNumber()))
                .thenReturn(flight);
        when(reservationMapper.reservationToReservationSTO(reservation))
                .thenReturn(reservationResponseDTO);

        ReservationResponseDTO makeReservation = reservationService.makeReservationForCustomerExists(reservationRequestDTO);

        assertThat(makeReservation).isNotNull();
    }

    @Test
    @Disabled
    void givenReservationResponseDTO_whenMakeReservationForWrongFlightNumber_thenReturnInvalidEntityException(){
        when(flightRepository.existingFlightNumber(reservationRequestDTO.getFlightNumber()))
                .thenReturn(false);

        exception = assertThrows(InvalidEntityException.class, ()->{
            reservationService.makeReservationForCustomerExists(reservationRequestDTO);
        });

        assertThat(exception.getMessage())
                .isEqualTo("Flight with num flight = " + reservationRequestDTO.getFlightNumber() + " does not exists in database");
        assertThat(HttpStatus.NOT_FOUND).isEqualTo(exception.getHttpStatus());
    }

    @Test
    @Disabled
    void givenReservationRequestDTO_whenMakeReservationEmptyCustomer_thenReturnInvalidEntityException() {
        exception = assertThrows(InvalidEntityException.class, ()->{
            reservationService.makeReservationForNewCustomer(invalidReservationNewCustomer);
        });

        assertThat(exception.getMessage()).isEqualTo("Your reservation contains some errors");
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(exception.getHttpStatus());
    }


    @Test
    @Disabled
    void givenReservationRequestDTO_whenMakeReservationForNewCustomer_thenReturnReservationResponseDTO() {
        when(ReservationValidator.validateReservation(reservationRequestDTO)).thenReturn(errors);
        when(reservationService.createNewCustomer(reservationRequestNewCustomerDTO)).thenReturn(customer);
        when(reservationService.createReservation(customer, flight)).thenReturn(reservationResponseDTO);

        ReservationResponseDTO reservationResponse = reservationService.makeReservationForNewCustomer(reservationRequestNewCustomerDTO);

        assertThat(reservationResponse).isNotNull();
    }

    @Test
    @Disabled
    void givenReservationRequestDTO_whenMakeReservationForWrongNumPassport_thenReturnInvalidEntityException(){
        when(customerRepository.existsCustomerByNumPassport(reservationRequestDTO.getNumPassport()))
                .thenReturn(false);

        exception = assertThrows(InvalidEntityException.class, () -> {
            reservationService.getFlightByNumFlight(invalidRequest);
        });

        assertThat(exception.getMessage())
                .isEqualTo("Customer with num passport = " + reservationRequestDTO.getNumPassport() + " does not exists in database");
        assertThat(HttpStatus.NOT_FOUND).isEqualTo(exception.getHttpStatus());
    }

    @Test
    @Disabled
    void givenReservationRequestDTO_whenGetFlightByNumFlight_thenReturnFlight(){
        when(customerRepository.existsCustomerByNumPassport(reservationRequestDTO.getNumPassport()))
                .thenReturn(true);
        given(flightRepository.findFlightByFlightNumber(reservationRequestDTO.getFlightNumber())).willReturn(flight);

        Flight saveFlight = reservationService.getFlightByNumFlight(reservationRequestDTO);

        assertThat(saveFlight).isNotNull();
    }

    @Test
    @Disabled
    void givenCustomerAndFlight_whenCreateReservation_thenReturnReservationResponseDTO() {
        given(reservationRepository.save(reservation)).willReturn(reservation);
        given(reservationMapper.reservationToReservationSTO(reservation)).willReturn(reservationResponseDTO);

        ReservationResponseDTO saveReservation = reservationService.createReservation(customer, flight);

        assertThat(saveReservation).isNotNull();
    }

    @Test
    @Disabled
    void givenRequestReservationRequestDTO_whenCreateNewCustomer_thenReturnCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer saveCustomerRes = reservationService.createNewCustomer(reservationRequestNewCustomerDTO);

        assertThat(saveCustomerRes).isNotNull();
        assertThat(firstName).isEqualTo(reservationRequestNewCustomerDTO.getFirstName());
        assertThat(lastName).isEqualTo(reservationRequestNewCustomerDTO.getLastName());
        assertThat(email).isEqualTo(reservationRequestNewCustomerDTO.getEmail());
        assertThat(birthday).isEqualTo(reservationRequestNewCustomerDTO.getBirthdate());
        assertThat(address).isEqualTo(reservationRequestNewCustomerDTO.getAddresses());
        assertThat(phoneNumber).isEqualTo(reservationRequestNewCustomerDTO.getTelephone());
        assertThat(numPassport).isEqualTo(reservationRequestNewCustomerDTO.getNumPassport());
    }
}