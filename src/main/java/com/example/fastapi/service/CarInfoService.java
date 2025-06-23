package com.example.fastapi.service;


import com.example.fastapi.dto.CarInfo;
import com.example.fastapi.repository.CarInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CarInfoService {

    private final CarInfoRepository carInfoRepository;

    @Autowired
    public CarInfoService(CarInfoRepository carInfoRepository) {
        this.carInfoRepository = carInfoRepository;
    }

    // Insert a new car record into MongoDB
    public CarInfo insertCarInfo(CarInfo carInfo) {
        // Optional: check if a car with this chassis number already exists
        if (carInfoRepository.existsByChassisNo(carInfo.getChassisNo())) {
            throw new IllegalArgumentException("Car with this chassis number already exists");
        }

        // Set current date and time if not provided
        if (carInfo.getDateTime() == null) {
            carInfo.setDateTime(LocalDateTime.now());
        }

        // Save the car information to MongoDB
        return carInfoRepository.save(carInfo);
    }
}
