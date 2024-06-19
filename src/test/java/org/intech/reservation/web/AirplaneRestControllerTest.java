package org.intech.reservation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.response.AirplaneResponseDTO;
import org.intech.reservation.roots.AirplaneEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.AirplaneService;
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
import static org.hamcrest.CoreMatchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AirplaneRestController.class)
class AirplaneRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AirplaneService airplaneService;
    @Autowired
    private ObjectMapper objectMapper;
    AirplaneResponseDTO airplaneResponseDTO;
    AirplaneRequestDTO airplaneRequestDTO;
    Long airplaneId = 1L;
    String model = "A750";
    String brand = "AIR FRANCE";
    Long yearMaking = 1990L;
    String Authorization = "Authorization";
    String Bearer = "Bearer ";
    String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ0NFVXRklPc1VRSzdzT2tJbGNDdmJKMkRoTTJrdHlxY0w0Tkc3cWsycUVRIn0.eyJleHAiOjE2ODYxNzkzNDksImlhdCI6MTY4NjE3ODE0OSwianRpIjoiYTA1ZTJlMDAtMjVlYS00OTA3LTk4MDgtMTViY2YwN2IyZjM2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9mbGlnaHQtcmVzZXJ2YXRpb24iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZmFlMTQxZjYtZGEyNC00MTlhLWFjZGEtYjA1MzkyMWViMjNiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZmxpZ2h0LWNsaWVudCIsInNlc3Npb25fc3RhdGUiOiI0ZDEzMjM3OS05MWMxLTRjNDItYjFlMS1iNjZhOGJiOGE5MTAiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtZmxpZ2h0LXJlc2VydmF0aW9uIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImZsaWdodC1jbGllbnQiOnsicm9sZXMiOlsiZmxpZ2h0X2FkbWluIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI0ZDEzMjM3OS05MWMxLTRjNDItYjFlMS1iNjZhOGJiOGE5MTAiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJNb2hhbWVkIEFsYmVydCIsInByZWZlcnJlZF91c2VybmFtZSI6ImFsYmVydCIsImdpdmVuX25hbWUiOiJNb2hhbWVkIiwiZmFtaWx5X25hbWUiOiJBbGJlcnQiLCJlbWFpbCI6ImFsYmVydEBnbWFpbC5jb20ifQ.S11a0yge5FXRdsN3JGvu6g0NHEhi-vgjpmIHcfPfx4EbKXPGWFDDb_9UnpM5UIf3mSNIvccDN9IMLSf2NXEKw6fniEklxNq923PLIZ_TtxnOQh_bHnErj3AdztIcUTdx_av8iIAomKW-UGZg1rQDa_QLJRoHeRKzPSJ3Q4_VyG2tOlgevX_2TYdpE2UvuoVuqFExvO2fQDJXGe7ago6F_McjuYwQvrERzbjzqual5W8nNRcrBHECy2K2u7Hit5c8lJq8_MGL2oLAVkGNZRs_TKemv2YecJ8Zn_cvzHMkIvRFRv751WtbAiIS-Z3vEbPGrEYJZBZ-QrADXsBZ0xqJ4w";

    @BeforeEach
    void setUp(){
        airplaneResponseDTO = new AirplaneResponseDTO();
            airplaneResponseDTO.setAirplaneId(airplaneId);
            airplaneResponseDTO.setModel(model);
            airplaneResponseDTO.setBrand(brand);
            airplaneResponseDTO.setYearMaking(yearMaking);

        airplaneRequestDTO = new AirplaneRequestDTO();
            airplaneRequestDTO.setModel(model);
            airplaneRequestDTO.setBrand(brand);
            airplaneRequestDTO.setYearMaking(yearMaking);
    }


    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirplaneRequestObject_whenCreateAirPlane_thenReturnSavedAirplane201() throws Exception {
        given(airplaneService.savePlane(any(AirplaneRequestDTO.class)))
                .willReturn(airplaneResponseDTO);

        ResultActions resultActions = mockMvc
                .perform(post(RootEndPoint.API_ROOT + AirplaneEndPoint.AIRPLANE_ENDPOINT)
                .header(Authorization, Bearer + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airplaneRequestDTO)));

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", is(airplaneResponseDTO.getModel())))
                .andExpect(jsonPath("$.brand", is(airplaneResponseDTO.getBrand())));
//                .andExpect(jsonPath("$.yearMaking", is(1990L)));

        ArgumentCaptor<AirplaneRequestDTO> captor = ArgumentCaptor.forClass(AirplaneRequestDTO.class);
        verify(airplaneService).savePlane(captor.capture());
        AirplaneRequestDTO capturedRequest = captor.getValue();
        assertEquals(model, capturedRequest.getModel());
        assertEquals(brand, capturedRequest.getBrand());
        assertEquals(yearMaking, capturedRequest.getYearMaking());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirplaneRequestObject_whenUpdateAirplane_thenReturnUpdateAirplane200() throws Exception {
          String modelUpdate = "TURK-950";
         String brandUpdate = "TURKISH AIRLINES";
         airplaneRequestDTO.setModel(modelUpdate);
         airplaneRequestDTO.setBrand(brandUpdate);
         airplaneResponseDTO.setModel(modelUpdate);
         airplaneResponseDTO.setBrand(brandUpdate);

         given(airplaneService.getAirplaneId(airplaneId)).willReturn(airplaneResponseDTO);
         given(airplaneService.updatePlane(eq(airplaneId), any(AirplaneRequestDTO.class))).willReturn(airplaneResponseDTO);

         ResultActions resultActions = mockMvc
                 .perform(put(RootEndPoint.API_ROOT + AirplaneEndPoint.AIRPLANE_ENDPOINT_ID, airplaneId)
                         .header(Authorization, Bearer + accessToken)
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(airplaneRequestDTO)));

         resultActions.andExpect(status().isOk())
                 .andDo(print())
                 .andExpect(jsonPath("$.model", is(airplaneResponseDTO.getModel())))
                 .andExpect(jsonPath("$.brand", is(airplaneResponseDTO.getBrand())));

         ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
         ArgumentCaptor<AirplaneRequestDTO> requestCaptor = ArgumentCaptor.forClass(AirplaneRequestDTO.class);
         verify(airplaneService).updatePlane(idCaptor.capture(), requestCaptor.capture());
         Long captureId = idCaptor.getValue();
         AirplaneRequestDTO capturedRequest = requestCaptor.getValue();
         assertEquals(airplaneId, captureId);
         assertEquals(modelUpdate, capturedRequest.getModel());
         assertEquals(brandUpdate, capturedRequest.getBrand());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirplaneList_whenGetAllPlane_thenReturnAllAirplane200() throws Exception {
        List<AirplaneResponseDTO> airplaneList = new ArrayList<>();
        airplaneList.add(airplaneResponseDTO);
        given(airplaneService.getAllPlane()).willReturn(airplaneList);

        ResultActions resultActions = mockMvc
                .perform(get(RootEndPoint.API_ROOT + AirplaneEndPoint.AIRPLANE_ENDPOINT)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(airplaneList.size())));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirplaneId_whenGetAirplaneId_thenReturn200() throws Exception {
        given(airplaneService.getAirplaneId(airplaneId)).willReturn(airplaneResponseDTO);

        ResultActions resultActions = mockMvc.
                perform(get(RootEndPoint.API_ROOT + AirplaneEndPoint.AIRPLANE_ENDPOINT_ID, airplaneId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model", is(airplaneResponseDTO.getModel())))
                .andExpect(jsonPath("$.brand", is(airplaneResponseDTO.getBrand())));

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(airplaneService).getAirplaneId(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(airplaneId, captureId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "flight_admin" })
    void givenAirplaneId_whenDeletePlane_thenReturn200() throws Exception {
        willDoNothing().given(airplaneService).deletePlane(airplaneId);

        ResultActions resultActions = mockMvc
                .perform(delete(RootEndPoint.API_ROOT + AirplaneEndPoint.AIRPLANE_ENDPOINT_ID, airplaneId)
                        .header(Authorization, Bearer + accessToken)
                );

        resultActions.andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(airplaneService).deletePlane(idCaptor.capture());
        Long captureId = idCaptor.getValue();
        assertEquals(airplaneId, captureId);
    }
}