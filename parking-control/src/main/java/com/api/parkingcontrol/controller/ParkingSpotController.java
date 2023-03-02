package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.dto.ParkingSpotDTO;
import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.service.ParkingSpotService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService service;

    public ParkingSpotController(ParkingSpotService service) {
        this.service = service;
    }

   @PostMapping("/save")
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO) {

        if (service.existsByLicensePlateCar(parkingSpotDTO.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }

       if (service.existsByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
       }

       if (service.existsByApartmentNumberAndBlock(parkingSpotDTO.getApartmentNumber(), parkingSpotDTO.getBlock())) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this "
               + "apartment or block!");
       }

        var spotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotDTO, spotModel);
        spotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(spotModel));
    }

    @GetMapping("/list-all")
    public ResponseEntity<Page<ParkingSpotModel>> findAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id",
    direction = Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(pageable));
    }

    @GetMapping("/list-by-id/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);

        return parkingSpotModelOptional.<ResponseEntity<Object>>map(
                parkingSpotModel -> ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);

        if (parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        service.deleteById(parkingSpotModelOptional.get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
        @RequestBody @Valid ParkingSpotDTO dto) {

        Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);

        if (parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found.");
        }

        var parkingSpot = new ParkingSpotModel();

        BeanUtils.copyProperties(dto, parkingSpot);
        parkingSpot.setId(parkingSpotModelOptional.get().getId());
        parkingSpot.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());

        return ResponseEntity.status(HttpStatus.OK).body(service.save(parkingSpot));

    }

}
