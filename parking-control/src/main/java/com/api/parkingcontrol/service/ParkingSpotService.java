package com.api.parkingcontrol.service;

import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpotService {

    final ParkingSpotRepository repository;

    public ParkingSpotService(ParkingSpotRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel spotModel) {
        return repository.save(spotModel);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {

        return repository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {

        return repository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentNumberAndBlock(String block, String apartmentNumber) {
        return repository.existsByApartmentNumberAndBlock(block, apartmentNumber);
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

    public Optional<ParkingSpotModel> findById(UUID id) {

        return repository.findById(id);
    }

    @Transactional
    public void deleteById(ParkingSpotModel parkingSpotModel) {

        repository.delete(parkingSpotModel);
    }
}
