package org.intech.reservation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intech.reservation.dtos.request.CustomerRequestDTO;
import org.intech.reservation.dtos.response.CustomerResponseDTO;
import org.intech.reservation.roots.CustomerEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerRestController.class)
class CustomerRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    @Autowired
    private ObjectMapper objectMapper;
    CustomerResponseDTO customerResponseDTO;
    CustomerRequestDTO customerRequestDTO;
    Long userId = 1L;
    String firstName = "Oumar";
    String lastName = "Sy";
    String birthday = "12-02-1998";
    Long phoneNumber = 756589956L;
    String address = "12 rue de la peace";
    String numPassport = "G12568";
    String email = "oumar@gmail.com";
    String Authorization = "Authorization";
    String Bearer = "Bearer ";
    String accessToken = "";

    @BeforeEach
    void setUp(){
        customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setFirstName(firstName);
        customerResponseDTO.setLastName(lastName);
        customerResponseDTO.setNumPassport(numPassport);
        customerResponseDTO.setEmail(email);
        customerResponseDTO.setTelephone(phoneNumber);
        customerResponseDTO.setBirthdate(birthday);
        customerResponseDTO.setAddresses(address);
        customerResponseDTO.setUserId(userId);

        customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setFirstName(firstName);
        customerRequestDTO.setLastName(lastName);
        customerRequestDTO.setNumPassport(numPassport);
        customerRequestDTO.setEmail(email);
        customerRequestDTO.setTelephone(phoneNumber);
        customerRequestDTO.setBirthdate(birthday);
        customerRequestDTO.setAddresses(address);


    }


    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenCustomerRequestObject_whenCreateCustomer_thenReturnSavedCustomer201() throws Exception {
        given(customerService.saveCustomer(any(CustomerRequestDTO.class)))
                .willReturn(customerResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(post(RootEndPoint.API_ROOT + CustomerEndPoint.CUSTOMER_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)));

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(customerResponseDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(customerResponseDTO.getLastName())))
                .andExpect(jsonPath("$.email", is(customerResponseDTO.getEmail())))
//                .andExpect(jsonPath("$.telephone", is(customerResponseDTO.getTelephone())))
                .andExpect(jsonPath("$.addresses", is(customerResponseDTO.getAddresses())))
                .andExpect(jsonPath("$.birthdate", is(customerResponseDTO.getBirthdate())))
                .andExpect(jsonPath("$.numPassport", is(customerResponseDTO.getNumPassport())));

        ArgumentCaptor<CustomerRequestDTO> captor = ArgumentCaptor.forClass(CustomerRequestDTO.class);
        verify(customerService).saveCustomer(captor.capture());
        CustomerRequestDTO capturedRequest = captor.getValue();
        assertEquals(firstName, capturedRequest.getFirstName());
        assertEquals(lastName, capturedRequest.getLastName());
        assertEquals(email, capturedRequest.getEmail());
        assertEquals(phoneNumber, capturedRequest.getTelephone());
        assertEquals(address, capturedRequest.getAddresses());
        assertEquals(birthday, capturedRequest.getBirthdate());
        assertEquals(numPassport, capturedRequest.getNumPassport());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenCustomerRequestObject_whenUpdateCustomer_thenReturnUpdateCustomer200() throws Exception {
        String firstNameUpdate = "Oumaruh";
        String emailUpdate = "oumaruh@gmail.com";
        customerRequestDTO.setFirstName(firstNameUpdate);
        customerRequestDTO.setEmail(emailUpdate);
        customerResponseDTO.setFirstName(firstNameUpdate);
        customerResponseDTO.setEmail(emailUpdate);

        given(customerService.getCustomerId(userId)).willReturn(customerResponseDTO);
        given(customerService.updateCustomer(eq(userId), any(CustomerRequestDTO.class))).willReturn(customerResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(put(RootEndPoint.API_ROOT + CustomerEndPoint.CUSTOMER_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(customerResponseDTO.getFirstName())))
                .andExpect(jsonPath("$.email", is(customerResponseDTO.getEmail())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CustomerRequestDTO> requestCaptor = ArgumentCaptor.forClass(CustomerRequestDTO.class);
        verify(customerService).updateCustomer(idCaptor.capture(), requestCaptor.capture());
        Long captureId = idCaptor.getValue();
        CustomerRequestDTO capturedRequest = requestCaptor.getValue();
        assertEquals(userId, captureId);
        assertEquals(firstNameUpdate, capturedRequest.getFirstName());
        assertEquals(emailUpdate, capturedRequest.getEmail());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenCustomerList_whenGetAllPlane_thenReturnAllCustomer200() throws Exception {
        List<CustomerResponseDTO> customerList = new ArrayList<>();
        customerList.add(customerResponseDTO);
        given(customerService.getAllCustomers()).willReturn(customerList);

        ResultActions resultActions = mockMvc
                .perform(get(RootEndPoint.API_ROOT + CustomerEndPoint.CUSTOMER_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(customerList.size())));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenCustomerId_whenGetCustomerId_thenReturn200() throws Exception {
        given(customerService.getCustomerId(userId)).willReturn(customerResponseDTO);

        ResultActions resultActions = mockMvc.
                perform(get(RootEndPoint.API_ROOT + CustomerEndPoint.CUSTOMER_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(customerResponseDTO.getFirstName())))
                .andExpect(jsonPath("$.email", is(customerResponseDTO.getEmail())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(customerService).getCustomerId(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(userId, captureId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenCustomerId_whenDeletePlane_thenReturn200() throws Exception {
        willDoNothing().given(customerService).deleteCustomer(userId);

        ResultActions resultActions = mockMvc
                .perform(delete(RootEndPoint.API_ROOT + CustomerEndPoint.CUSTOMER_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(customerService).deleteCustomer(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(userId, captureId);
    }
}