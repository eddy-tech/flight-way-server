package org.intech.reservation.integrations.containers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.intech.reservation.dtos.response.EmployeeResponseDTO;
import org.intech.reservation.repositories.EmployeeRepository;
import org.intech.reservation.roots.EmployeeEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.EmployeeService;
import org.intech.reservation.web.EmployeeRestController;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
class EmployeeRestControllerITestContainer extends AbstractionContainerBaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    EmployeeResponseDTO employeeResponseDTO;
    EmployeeRequestDTO employeeRequestDTO;
    Long userId = 1L;
    String firstName = "Oumar";
    String lastName = "Sy";
    String birthday = "12-02-1998";
    Long phoneNumber = 756589956L;
    String address = "12 rue de la peace";
    Long numEmp = 5689256L;
    String email = "oumar@gmail.com";
    String Authorization = "Authorization";
    String Bearer = "Bearer ";
    String accessToken = "";

    @BeforeEach
    void setUp(){
        employeeResponseDTO = new EmployeeResponseDTO();
        employeeResponseDTO.setFirstName(firstName);
        employeeResponseDTO.setLastName(lastName);
        employeeResponseDTO.setNumEmp(numEmp);
        employeeResponseDTO.setEmail(email);
        employeeResponseDTO.setTelephone(phoneNumber);
        employeeResponseDTO.setBirthdate(birthday);
        employeeResponseDTO.setAddresses(address);
        employeeResponseDTO.setUserId(userId);

        employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFirstName(firstName);
        employeeRequestDTO.setLastName(lastName);
        employeeRequestDTO.setNumEmp(numEmp);
        employeeRequestDTO.setEmail(email);
        employeeRequestDTO.setTelephone(phoneNumber);
        employeeRequestDTO.setBirthdate(birthday);
        employeeRequestDTO.setAddresses(address);
    }

    @AfterEach
    public void tearDown(){
        employeeRepository.deleteAll();
    }


    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenEmployeeRequestObject_whenCreateEmployee_thenReturnSavedEmployee201() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(post(RootEndPoint.API_ROOT + EmployeeEndPoint.EMPLOYEE_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestDTO)));

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employeeResponseDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeResponseDTO.getLastName())))
                .andExpect(jsonPath("$.email", is(employeeResponseDTO.getEmail())))
//                .andExpect(jsonPath("$.telephone", is(employeeResponseDTO.getTelephone())))
                .andExpect(jsonPath("$.addresses", is(employeeResponseDTO.getAddresses())))
                .andExpect(jsonPath("$.birthdate", is(employeeResponseDTO.getBirthdate())));
//                .andExpect(jsonPath("$.numEmp", is(employeeResponseDTO.getNumEmp())));

        ArgumentCaptor<EmployeeRequestDTO> captor = ArgumentCaptor.forClass(EmployeeRequestDTO.class);
        EmployeeRequestDTO capturedRequest = captor.getValue();
        assertEquals(firstName, capturedRequest.getFirstName());
        assertEquals(lastName, capturedRequest.getLastName());
        assertEquals(email, capturedRequest.getEmail());
        assertEquals(phoneNumber, capturedRequest.getTelephone());
        assertEquals(address, capturedRequest.getAddresses());
        assertEquals(birthday, capturedRequest.getBirthdate());
        assertEquals(numEmp, capturedRequest.getNumEmp());
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenEmployeeRequestObject_whenUpdateEmployee_thenReturnUpdateEmployee200() throws Exception {
        String firstNameUpdate = "Oumaruh";
        String emailUpdate = "oumaruh@gmail.com";
        employeeRequestDTO.setFirstName(firstNameUpdate);
        employeeRequestDTO.setEmail(emailUpdate);
        employeeResponseDTO.setFirstName(firstNameUpdate);
        employeeResponseDTO.setEmail(emailUpdate);

        ResultActions resultActions = mockMvc
                .perform(put(RootEndPoint.API_ROOT + EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestDTO)));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employeeResponseDTO.getFirstName())))
                .andExpect(jsonPath("$.email", is(employeeResponseDTO.getEmail())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<EmployeeRequestDTO> requestCaptor = ArgumentCaptor.forClass(EmployeeRequestDTO.class);
        Long captureId = idCaptor.getValue();
        EmployeeRequestDTO capturedRequest = requestCaptor.getValue();
        assertEquals(userId, captureId);
        assertEquals(firstNameUpdate, capturedRequest.getFirstName());
        assertEquals(emailUpdate, capturedRequest.getEmail());
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenEmployeeList_whenGetAllPlane_thenReturnAllEmployee200() throws Exception {
        List<EmployeeResponseDTO> customerList = new ArrayList<>();
        customerList.add(employeeResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(get(RootEndPoint.API_ROOT + EmployeeEndPoint.EMPLOYEE_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(customerList.size())));
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenEmployeeId_whenGetEmployeeId_thenReturn200() throws Exception {
        ResultActions resultActions = mockMvc.
                perform(get(RootEndPoint.API_ROOT + EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employeeResponseDTO.getFirstName())))
                .andExpect(jsonPath("$.email", is(employeeResponseDTO.getEmail())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        Long captureId = idCaptor.getValue();
        assertEquals(userId, captureId);
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenEmployeeId_whenDeletePlane_thenReturn200() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(delete(RootEndPoint.API_ROOT + EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        Long captureId = idCaptor.getValue();
        assertEquals(userId, captureId);
    }
}