package org.intech.reservation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intech.reservation.dtos.request.FlightRequestDTO;
import org.intech.reservation.dtos.response.FlightResponseDTO;
import org.intech.reservation.entities.Airplane;
import org.intech.reservation.entities.Airport;
import org.intech.reservation.roots.FlightEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.FlightService;
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
@WebMvcTest(FlightRestController.class)
class FlightRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FlightService flightService;
    @Autowired
    private ObjectMapper objectMapper;
    FlightResponseDTO flightResponseDTO;
    FlightRequestDTO flightRequestDTO;
    Airplane airplane;
    Airport airport;

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
    String Authorization = "Authorization";
    String Bearer = "Bearer ";
    String accessToken = "";

    @BeforeEach
    void setUp(){
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

        flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.setFlightNumber(flightNumber);
        flightResponseDTO.setFlightId(flightId);
        flightResponseDTO.setCityStart(cityStart);
        flightResponseDTO.setCityArrive(cityArrive);
        flightResponseDTO.setHourDateStart(hourDateStart);
        flightResponseDTO.setHourDateArrive(hourDateArrive);
        flightResponseDTO.setNumberOfPlace(numberOfPlace);
        flightResponseDTO.setAirport(airport);
        flightResponseDTO.setAirplane(airplane);

        flightRequestDTO = new FlightRequestDTO();
        flightRequestDTO.setCityStart(cityStart);
        flightRequestDTO.setCityArrive(cityArrive);
        flightRequestDTO.setHourDateArrive(hourDateArrive);
        flightRequestDTO.setHourDateStart(hourDateStart);
        flightRequestDTO.setNumberOfPlace(numberOfPlace);
    }


    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenFlightRequestObject_whenCreateFlight_thenReturnSavedFlight201() throws Exception {
        given(flightService.saveFlight(any(FlightRequestDTO.class)))
                .willReturn(flightResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(post(RootEndPoint.API_ROOT + FlightEndPoint.FLIGHT_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightRequestDTO)));

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityStart", is(flightResponseDTO.getCityStart())))
                .andExpect(jsonPath("$.cityArrive", is(flightResponseDTO.getCityArrive())))
                .andExpect(jsonPath("$.hourDateStart", is(flightResponseDTO.getHourDateStart())))
                .andExpect(jsonPath("$.hourDateArrive", is(flightResponseDTO.getHourDateArrive())))
//                .andExpect(jsonPath("$.numberOfPlace", is(flightResponseDTO.getNumberOfPlace())))
                .andExpect(jsonPath("$.flightNumber", is(flightResponseDTO.getFlightNumber())));
//                .andExpect(jsonPath("$.airport", is(flightResponseDTO.getAirport())))
//                .andExpect(jsonPath("$.airplane", is(flightResponseDTO.getAirplane())));

        ArgumentCaptor<FlightRequestDTO> captor = ArgumentCaptor.forClass(FlightRequestDTO.class);
        verify(flightService).saveFlight(captor.capture());
        FlightRequestDTO capturedRequest = captor.getValue();
        assertEquals(cityStart, capturedRequest.getCityStart());
        assertEquals(cityArrive, capturedRequest.getCityArrive());
        assertEquals(hourDateStart, capturedRequest.getHourDateStart());
        assertEquals(hourDateArrive, capturedRequest.getHourDateArrive());
        assertEquals(numberOfPlace, capturedRequest.getNumberOfPlace());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenFlightRequestObject_whenUpdateFlight_thenReturnUpdateFlight200() throws Exception {
        String cityStartUpdate = "Istanbul";
        String cityArriveUpdate = "Milano";
        flightRequestDTO.setCityStart(cityStartUpdate);
        flightRequestDTO.setCityArrive(cityArriveUpdate);
        flightResponseDTO.setCityStart(cityStartUpdate);
        flightResponseDTO.setCityArrive(cityArriveUpdate);

        given(flightService.getFlightId(flightId)).willReturn(flightResponseDTO);
        given(flightService.updateFlight(eq(flightId), any(FlightRequestDTO.class))).willReturn(flightResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(put(RootEndPoint.API_ROOT + FlightEndPoint.FLIGHT_ENDPOINT_ID, flightId)
                        .header(Authorization, Bearer + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightRequestDTO)));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.cityStart", is(flightResponseDTO.getCityStart())))
                .andExpect(jsonPath("$.cityArrive", is(flightResponseDTO.getCityArrive())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<FlightRequestDTO> requestCaptor = ArgumentCaptor.forClass(FlightRequestDTO.class);
        verify(flightService).updateFlight(idCaptor.capture(), requestCaptor.capture());
        Long captureId = idCaptor.getValue();
        FlightRequestDTO capturedRequest = requestCaptor.getValue();
        assertEquals(flightId, captureId);
        assertEquals(cityStartUpdate, capturedRequest.getCityStart());
        assertEquals(cityArriveUpdate, capturedRequest.getCityArrive());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenFlightList_whenGetAllPlane_thenReturnAllFlight200() throws Exception {
        List<FlightResponseDTO> flightList = new ArrayList<>();
        flightList.add(flightResponseDTO);
        given(flightService.getAllFlight()).willReturn(flightList);

        ResultActions resultActions = mockMvc
                .perform(get(RootEndPoint.API_ROOT + FlightEndPoint.FLIGHT_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(flightList.size())));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenHoursAndCityParams_whenSearchFlight_thenReturnAllFlightList202() throws Exception {
        List<FlightResponseDTO> flightList = new ArrayList<>();
        flightList.add(flightResponseDTO);
        given(flightService.searchFlight(cityStart, cityArrive, hourDateStart, hourDateArrive))
                .willReturn(flightList);

        ResultActions resultActions = mockMvc
                .perform(get(RootEndPoint.API_ROOT + FlightEndPoint.SEARCH_FLIGHT_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                        .param("cityStart", cityStart)
                        .param("cityArrive", cityArrive)
                        .param("hourDateStart", hourDateStart)
                        .param("hourDateArrive", hourDateArrive)
                );

        resultActions.andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.size()", is(flightList.size())));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenFlightId_whenGetFlightId_thenReturn200() throws Exception {
        given(flightService.getFlightId(flightId)).willReturn(flightResponseDTO);

        ResultActions resultActions = mockMvc.
                perform(get(RootEndPoint.API_ROOT + FlightEndPoint.FLIGHT_ENDPOINT_ID, flightId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityStart", is(flightResponseDTO.getCityStart())))
                .andExpect(jsonPath("$.cityArrive", is(flightResponseDTO.getCityArrive())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flightService).getFlightId(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(flightId, captureId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin", "flight_employee" })
    void givenFlightId_whenDeletePlane_thenReturn200() throws Exception {
        willDoNothing().given(flightService).deleteFlight(flightId);

        ResultActions resultActions = mockMvc
                .perform(delete(RootEndPoint.API_ROOT + FlightEndPoint.FLIGHT_ENDPOINT_ID, flightId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flightService).deleteFlight(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(flightId, captureId);
    }
}