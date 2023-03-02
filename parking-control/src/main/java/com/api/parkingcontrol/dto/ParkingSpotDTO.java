package com.api.parkingcontrol.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingSpotDTO {

    public ParkingSpotDTO() {
        super();
    }

    public ParkingSpotDTO(String parkingSpotNumber, String licensePlateCar, String brandCar, String modelCar,
        String colorCar, String responsibleName, String apartmentNumber, String block) {
        this.parkingSpotNumber = parkingSpotNumber;
        this.licensePlateCar = licensePlateCar;
        this.brandCar = brandCar;
        this.modelCar = modelCar;
        this.colorCar = colorCar;
        this.responsibleName = responsibleName;
        this.apartmentNumber = apartmentNumber;
        this.block = block;
    }

    @NotBlank
    private String parkingSpotNumber;

    @NotBlank
    @Size(max = 7)
    private String licensePlateCar;

    @NotBlank
    private String brandCar;

    @NotBlank
    private String modelCar;

    @NotBlank
    private String colorCar;

    @NotBlank
    private String responsibleName;

    @NotBlank
    private String apartmentNumber;

    @NotBlank
    private String block;

}
