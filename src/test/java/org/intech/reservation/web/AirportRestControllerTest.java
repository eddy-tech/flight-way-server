package org.intech.reservation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intech.reservation.dtos.request.AirportRequestDTO;

import org.intech.reservation.dtos.response.AirportResponseDTO;
import org.intech.reservation.roots.AirportEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.AirplaneService;
import org.intech.reservation.services.interfaces.AirportService;
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
@WebMvcTest(AirportRestController.class)
class AirportRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AirportService airportService;
    @Autowired
    private ObjectMapper objectMapper;
    AirportResponseDTO airportResponseDTO;
    AirportRequestDTO airportRequestDTO;
    Long airportId = 1L;
    String nameAirport = "Charles de Gaulle";
    String nameCity = "Paris";
    String nameCountry = "France";
    String Authorization = "Authorization";
    String Bearer = "Bearer ";
    String accessToken = "";

    @BeforeEach
    void setUp(){
        airportResponseDTO = new AirportResponseDTO();
        airportResponseDTO.setAirportId(airportId);
        airportResponseDTO.setNameAirport(nameAirport);
        airportResponseDTO.setCountry(nameCountry);
        airportResponseDTO.setCity(nameCity);

        airportRequestDTO = new AirportRequestDTO();
        airportRequestDTO.setNameAirport(nameAirport);
        airportRequestDTO.setCountry(nameCountry);
        airportRequestDTO.setCity(nameCity);
    }


    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirportRequestObject_whenCreateAirport_thenReturnSavedAirport201() throws Exception {
        given(airportService.saveAirport(any(AirportRequestDTO.class)))
                .willReturn(airportResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(post(RootEndPoint.API_ROOT + AirportEndPoint.AIRPORT_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airportRequestDTO)));

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nameAirport", is(airportResponseDTO.getNameAirport())))
                .andExpect(jsonPath("$.city", is(airportResponseDTO.getCity())))
                .andExpect(jsonPath("$.country", is(airportResponseDTO.getCountry())));

        ArgumentCaptor<AirportRequestDTO> captor = ArgumentCaptor.forClass(AirportRequestDTO.class);
        verify(airportService).saveAirport(captor.capture());
        AirportRequestDTO capturedRequest = captor.getValue();
        assertEquals(nameAirport, capturedRequest.getNameAirport());
        assertEquals(nameCity, capturedRequest.getCity());
        assertEquals(nameCountry, capturedRequest.getCountry());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirportRequestObject_whenUpdateAirport_thenReturnUpdateAirport200() throws Exception {
        String nameAirportUpdate = "TURKISH AIRLINES";
        String cityUpdate = "Istanbul";
        airportRequestDTO.setNameAirport(nameAirportUpdate);
        airportRequestDTO.setCity(cityUpdate);
        airportResponseDTO.setNameAirport(nameAirportUpdate);
        airportResponseDTO.setCity(cityUpdate);

        given(airportService.getAirportId(airportId)).willReturn(airportResponseDTO);
        given(airportService.updateAirport(eq(airportId), any(AirportRequestDTO.class))).willReturn(airportResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(put(RootEndPoint.API_ROOT + AirportEndPoint.AIRPORT_ENDPOINT_ID, airportId)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airportRequestDTO)));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nameAirport", is(airportResponseDTO.getNameAirport())))
                .andExpect(jsonPath("$.city", is(airportResponseDTO.getCity())))
                .andExpect(jsonPath("$.country", is(airportResponseDTO.getCountry())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<AirportRequestDTO> requestCaptor = ArgumentCaptor.forClass(AirportRequestDTO.class);
        verify(airportService).updateAirport(idCaptor.capture(), requestCaptor.capture());
        Long captureId = idCaptor.getValue();
        AirportRequestDTO capturedRequest = requestCaptor.getValue();
        assertEquals(airportId, captureId);
        assertEquals(nameAirportUpdate, capturedRequest.getNameAirport());
        assertEquals(cityUpdate, capturedRequest.getCity());
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirportList_whenGetAllPlane_thenReturnAllAirport200() throws Exception {
        List<AirportResponseDTO> airportList = new ArrayList<>();
        airportList.add(airportResponseDTO);
        given(airportService.getAllAirport()).willReturn(airportList);

        ResultActions resultActions = mockMvc
                .perform(get(RootEndPoint.API_ROOT + AirportEndPoint.AIRPORT_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(airportList.size())));
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirportId_whenGetAirportId_thenReturn200() throws Exception {
        given(airportService.getAirportId(airportId)).willReturn(airportResponseDTO);

        ResultActions resultActions = mockMvc.
                perform(get(RootEndPoint.API_ROOT + AirportEndPoint.AIRPORT_ENDPOINT_ID, airportId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameAirport", is(airportResponseDTO.getNameAirport())))
                .andExpect(jsonPath("$.city", is(airportResponseDTO.getCity())))
                .andExpect(jsonPath("$.country", is(airportResponseDTO.getCountry())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(airportService).getAirportId(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(airportId, captureId);
    }

    @Test
     @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirportId_whenDeletePlane_thenReturn200() throws Exception {
        willDoNothing().given(airportService).deleteAirport(airportId);

        ResultActions resultActions = mockMvc
                .perform(delete(RootEndPoint.API_ROOT + AirportEndPoint.AIRPORT_ENDPOINT_ID, airportId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(airportService).deleteAirport(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(airportId, captureId);
    }
}