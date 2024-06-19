package org.intech.reservation.services.impl;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intech.reservation.dtos.request.AirplaneRequestDTO;
import org.intech.reservation.dtos.response.AirplaneResponseDTO;
import org.intech.reservation.entities.Airplane;
import org.intech.reservation.exceptions.EntityNotFoundException;
import org.intech.reservation.exceptions.InvalidEntityException;
import org.intech.reservation.mappers.AirplaneMapper;
import org.intech.reservation.repositories.AirplaneRepository;
import org.intech.reservation.services.interfaces.AirplaneService;
import org.intech.reservation.validators.AirplaneValidator;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AirplaneServiceImpl implements AirplaneService {
    private AirplaneRepository planeRepository;
    private AirplaneMapper airplaneMapper;

    @Override
    public AirplaneResponseDTO savePlane(AirplaneRequestDTO airplaneRequestDto) {
        List<String> errors = AirplaneValidator.validatePlane(airplaneRequestDto);
        if(!errors.isEmpty()){
            log.error("Invalid airplane data");
            throw new InvalidEntityException("Airplane contains invalid data", errors);
        }

        Airplane airplane = airplaneMapper.airplaneRequestDTOToAirplane(airplaneRequestDto);
        Airplane saveAirplane = planeRepository.save(airplane);

        return airplaneMapper.airplaneToPlaneResponseDTO(saveAirplane);
    }


    @Override
    public AirplaneResponseDTO updatePlane(Long airplaneId, AirplaneRequestDTO airplaneRequestDto) {
        List<String> errors = AirplaneValidator.validatePlane(airplaneRequestDto);
        if(!errors.isEmpty()){
            log.error("Invalid airplane data");
            throw new InvalidEntityException("Invalid airplane data", errors);
        }

        Airplane existingAirplane = getExistingAirplaneById(airplaneId);
        existingAirplane.setBrand(airplaneRequestDto.getBrand());
        existingAirplane.setModel(airplaneRequestDto.getModel());
        existingAirplane.setYearMaking(airplaneRequestDto.getYearMaking());
        Airplane updateAirplane = planeRepository.save(existingAirplane);
        return airplaneMapper.airplaneToPlaneResponseDTO(updateAirplane);
    }

    public Airplane getExistingAirplaneById(Long airplaneId){
        if(airplaneId == null){
            log.error("Airplane ID is null");
            return null;
        }

        return planeRepository.findById(airplaneId).orElseThrow(()->
                new EntityNotFoundException(
                        "Airplane with ID = "+ airplaneId + " does not exist in DataBase"
                ));
    }

    @Override
    public List<AirplaneResponseDTO> getAllPlane() {
        List<Airplane> airplaneList = planeRepository.findAll();

        return airplaneList.stream()
                .map(plane -> airplaneMapper.airplaneToPlaneResponseDTO(plane))
                .collect(Collectors.toList());
    }

    @Override
    public AirplaneResponseDTO getAirplaneId(Long airplaneId) {
        Airplane airplane = getExistingAirplaneById(airplaneId);
        return airplaneMapper.airplaneToPlaneResponseDTO(airplane);
    }

    @Override
    public void deletePlane(Long airplaneId) {
        if(airplaneId == null){
            log.error("Airplane ID is null");
            return;
        }

        planeRepository.deleteById(airplaneId);
    }
}
