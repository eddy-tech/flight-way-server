package org.intech.reservation.web;

import lombok.AllArgsConstructor;
import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.response.AirplaneResponseDTO;
import org.intech.reservation.roots.AirplaneEndPoint;
import org.intech.reservation.roots.RootEndPoint;
import org.intech.reservation.services.interfaces.AirplaneService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(RootEndPoint.API_ROOT)
@CrossOrigin("*")
public class AirplaneRestController {
    private AirplaneService airplaneService;

    public AirplaneRestController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @PostMapping(AirplaneEndPoint.AIRPLANE_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('flight_admin')")
    public AirplaneResponseDTO savePlane(@RequestBody AirplaneRequestDTO airplaneRequestDto) {
        return airplaneService.savePlane(airplaneRequestDto);
    }

    @PutMapping(AirplaneEndPoint.AIRPLANE_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public AirplaneResponseDTO updatePlane(@PathVariable(name = "id") Long airplaneId, @RequestBody AirplaneRequestDTO airplaneRequestDto) {
        return airplaneService.updatePlane(airplaneId, airplaneRequestDto);
    }

    @GetMapping(AirplaneEndPoint.AIRPLANE_ENDPOINT)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public List<AirplaneResponseDTO> getAllPlane() {
        return airplaneService.getAllPlane();
    }

    @GetMapping(AirplaneEndPoint.AIRPLANE_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public AirplaneResponseDTO getAirplaneId(@PathVariable(name = "id") Long airplaneId) {
        return airplaneService.getAirplaneId(airplaneId);
    }

    @DeleteMapping(AirplaneEndPoint.AIRPLANE_ENDPOINT_ID)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('flight_admin')")
    public void deletePlane(@PathVariable(name = "id") Long planeId) {
        airplaneService.deletePlane(planeId);
    }
}
