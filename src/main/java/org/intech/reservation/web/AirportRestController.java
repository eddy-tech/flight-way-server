package org.intech.reservation.web;

import lombok.AllArgsConstructor;
import org.intech.reservation.dtos.request.AirportRequestDTO;
import org.intech.reservation.dtos.response.AirportResponseDTO;
import org.intech.reservation.roots.AirportEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(RootEndPoint.API_ROOT)
@CrossOrigin("*")
public class AirportRestController {
    private AirportService airportService;

    @PostMapping(AirportEndPoint.AIRPORT_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('flight_admin')")
    public AirportResponseDTO saveAirport(@RequestBody AirportRequestDTO airportRequestDto) {
        return airportService.saveAirport(airportRequestDto);
    }

    @PutMapping(AirportEndPoint.AIRPORT_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public AirportResponseDTO updateAirport(@PathVariable(name = "id") Long airportId, @RequestBody AirportRequestDTO airportRequestDto) {
        return airportService.updateAirport(airportId, airportRequestDto);
    }

    @GetMapping(AirportEndPoint.AIRPORT_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public List<AirportResponseDTO> getAllAirport() {
        return airportService.getAllAirport();
    }

    @GetMapping(AirportEndPoint.AIRPORT_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public AirportResponseDTO getAirportId(@PathVariable(name = "id") Long airportId) {
        return airportService.getAirportId(airportId);
    }

    @DeleteMapping(AirportEndPoint.AIRPORT_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public void deleteAirport(@PathVariable(name = "id") Long airportId) {
        airportService.deleteAirport(airportId);
    }
}
