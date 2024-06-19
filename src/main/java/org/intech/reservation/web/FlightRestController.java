package org.intech.reservation.web;

import lombok.AllArgsConstructor;
import org.intech.reservation.dtos.request.FlightRequestDTO;
import org.intech.reservation.dtos.response.FlightResponseDTO;
import org.intech.reservation.roots.FlightEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(RootEndPoint.API_ROOT)
@CrossOrigin("*")
public class FlightRestController {
    private FlightService flightService;

    @PostMapping(FlightEndPoint.FLIGHT_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponseDTO saveFlight(@RequestBody FlightRequestDTO flightRequestDto) {
        return flightService.saveFlight(flightRequestDto);
    }

    @PutMapping(FlightEndPoint.FLIGHT_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    public FlightResponseDTO updateFlight(@PathVariable(name = "id") Long flightId, @RequestBody FlightRequestDTO flightRequestDto) {
        return flightService.updateFlight(flightId, flightRequestDto);
    }

    @GetMapping(FlightEndPoint.FLIGHT_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    public List<FlightResponseDTO> getAllFlight() {
        return flightService.getAllFlight();
    }


    @GetMapping(FlightEndPoint.SEARCH_FLIGHT_ENDPOINT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<FlightResponseDTO> searchFlight(
            @RequestParam("cityStart") String cityStart,
            @RequestParam("cityArrive") String cityArrive,
            @RequestParam("hourDateStart") String hourDateStart,
            @RequestParam("hourDateArrive") String hourDateArrive){
        return flightService.searchFlight(cityStart, cityArrive, hourDateStart, hourDateArrive);
    }

    @GetMapping(FlightEndPoint.FLIGHT_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    public FlightResponseDTO getFlightId(@PathVariable(name = "id") Long flightId) {
        return flightService.getFlightId(flightId);
    }

    @DeleteMapping(FlightEndPoint.FLIGHT_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFlight(@PathVariable(name = "id") Long flightId) {
        flightService.deleteFlight(flightId);
    }
}
