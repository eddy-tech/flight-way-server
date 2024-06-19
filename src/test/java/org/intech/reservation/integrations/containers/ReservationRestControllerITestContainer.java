package org.intech.reservation.integrations.containers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.ReservationRequestDTO;
import org.intech.reservation.dtos.request.ReservationRequestNewCustomerDTO;
import org.intech.reservation.dtos.response.ReservationResponseDTO;
import org.intech.reservation.entities.Airplane;
import org.intech.reservation.entities.Airport;
import org.intech.reservation.entities.Customer;
import org.intech.reservation.entities.Flight;
import org.intech.reservation.repositories.ReservationRepository;
import org.intech.reservation.roots.ReservationEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.ReservationService;
import org.intech.reservation.web.ReservationRestController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
class ReservationRestControllerITestContainer extends AbstractionContainerBaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    ReservationRequestDTO reservationRequestDTO;
    ReservationResponseDTO reservationResponseDTO;
    ReservationRequestNewCustomerDTO reservationRequestNewCustomerDTO;
    Flight flight;
    Customer customer;
    Airplane airplane;
    Airport airport;

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
    String Authorization = "Authorization";
    String Bearer = "Bearer ";
    String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ0NFVXRklPc1VRSzdzT2tJbGNDdmJKMkRoTTJrdHlxY0w0Tkc3cWsycUVRIn0.eyJleHAiOjE2ODYxODAyNTUsImlhdCI6MTY4NjE3OTA1NSwianRpIjoiNDEyNDEzMGUtYTZkZC00MWM2LTllYTItMzc5NWI4NTYxZDc4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9mbGlnaHQtcmVzZXJ2YXRpb24iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZmFlMTQxZjYtZGEyNC00MTlhLWFjZGEtYjA1MzkyMWViMjNiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZmxpZ2h0LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI5MzE1ZTQxYS05YjhmLTQ1ZjEtYjY0Zi0yNGIwNzVhNzA3NjYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtZmxpZ2h0LXJlc2VydmF0aW9uIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImZsaWdodC1jbGllbnQiOnsicm9sZXMiOlsiZmxpZ2h0X2FkbWluIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI5MzE1ZTQxYS05YjhmLTQ1ZjEtYjY0Zi0yNGIwNzVhNzA3NjYiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJNb2hhbWVkIEFsYmVydCIsInByZWZlcnJlZF91c2VybmFtZSI6ImFsYmVydCIsImdpdmVuX25hbWUiOiJNb2hhbWVkIiwiZmFtaWx5X25hbWUiOiJBbGJlcnQiLCJlbWFpbCI6ImFsYmVydEBnbWFpbC5jb20ifQ.c0wSKsagAEwT38czZOdhYD-qnjL4HQWoMZhLjM05Rv6zLTDy9UMAMDLZ5WNdyFQQAACCEPwstG0GKH0Mbvum1hFuTeha6UG07L0XrDcTm7zQqSPdcqdPShUG58QiBiAgUOo2GJNXd1zNiraUKHf4sGkb_XBX-GIFCzEXf_89X4Lk7S-z1BU3KE3xOYqHjVzk7uCcaAOsYV6zphQKo_LlQSMe5wnIm_JNK7piRmC_lKQffNSxuxQyjWKhhLkKIk9rJkaOKNOKKpgg9ZJcV0ZlF1tJo_I1vwLbQOZjc_FMRMCoq-2t7BgmLvh48AjnZk9huDOVT_43OMh_It-5OkpBzw";

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    public void tearDown(){
        reservationRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee", "flight_customer"})
    void makeReservationForCustomerExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(RootEndPoint.API_ROOT + ReservationEndPoint.RESERVATION_ENDPOINT)
                .header(Authorization, Bearer + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationRequestDTO)));

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<ReservationRequestDTO> captor = ArgumentCaptor.forClass(ReservationRequestDTO.class);
        ReservationRequestDTO captureRequest = captor.getValue();
        assertEquals(flightNumber, captureRequest.getFlightNumber());
        assertEquals(numPassport, captureRequest.getNumPassport());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee", "flight_customer"})
    void makeReservationForNewCustomer() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(RootEndPoint.API_ROOT + ReservationEndPoint.CUSTOM_RESERVATION_ENDPOINT)
                .header(Authorization, Bearer + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationRequestNewCustomerDTO)));

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<ReservationRequestNewCustomerDTO> captor = ArgumentCaptor.forClass(ReservationRequestNewCustomerDTO.class);
        ReservationRequestNewCustomerDTO captureRequest = captor.getValue();
        assertEquals(flightNumber, captureRequest.getFlightNumber());
        assertEquals(numPassport, captureRequest.getNumPassport());
        assertEquals(firstName, captureRequest.getFirstName());
        assertEquals(lastName, captureRequest.getLastName());
        assertEquals(email, captureRequest.getEmail());
        assertEquals(birthday, captureRequest.getBirthdate());
        assertEquals(address, captureRequest.getAddresses());
        assertEquals(phoneNumber, captureRequest.getTelephone());
    }


}