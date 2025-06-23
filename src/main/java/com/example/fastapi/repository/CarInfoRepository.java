package com.example.fastapi.repository;

import com.example.fastapi.dto.CarInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarInfoRepository extends MongoRepository<CarInfo, String> {

    // Check if a car with the given chassis number already exists
    boolean existsByChassisNo(String chassisNo);

    // Retrieve a car by its chassis number
    CarInfo findByChassisNo(String chassisNo);

    CarInfo findByLicensePlate(String licensePlate);
}
