package org.intech.reservation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intech.reservation.dtos.request.EmployeeRequestDTO;
import org.intech.reservation.dtos.response.EmployeeResponseDTO;
import org.intech.reservation.roots.EmployeeEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.EmployeeService;
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
@WebMvcTest(EmployeeRestController.class)
class EmployeeRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
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


    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenEmployeeRequestObject_whenCreateEmployee_thenReturnSavedEmployee201() throws Exception {
        given(employeeService.saveEmployee(any(EmployeeRequestDTO.class)))
                .willReturn(employeeResponseDTO);

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
        verify(employeeService).saveEmployee(captor.capture());
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

        given(employeeService.getEmployeeId(userId)).willReturn(employeeResponseDTO);
        given(employeeService.updateEmployee(eq(userId), any(EmployeeRequestDTO.class))).willReturn(employeeResponseDTO);

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
        verify(employeeService).updateEmployee(idCaptor.capture(), requestCaptor.capture());
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
        given(employeeService.getAllEmployee()).willReturn(customerList);

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
        given(employeeService.getEmployeeId(userId)).willReturn(employeeResponseDTO);

        ResultActions resultActions = mockMvc.
                perform(get(RootEndPoint.API_ROOT + EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employeeResponseDTO.getFirstName())))
                .andExpect(jsonPath("$.email", is(employeeResponseDTO.getEmail())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(employeeService).getEmployeeId(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(userId, captureId);
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenEmployeeId_whenDeletePlane_thenReturn200() throws Exception {
        willDoNothing().given(employeeService).deleteEmployee(userId);

        ResultActions resultActions = mockMvc
                .perform(delete(RootEndPoint.API_ROOT + EmployeeEndPoint.EMPLOYEE_ENDPOINT_ID, userId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(employeeService).deleteEmployee(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(userId, captureId);
    }
}