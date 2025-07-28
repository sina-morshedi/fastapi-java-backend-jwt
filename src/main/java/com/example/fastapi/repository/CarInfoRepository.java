package com.example.fastapi.repository;

import com.example.fastapi.dboModel.CarInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
public interface CarInfoRepository extends MongoRepository<CarInfo, String> {

    // Check if a car with the given chassis number already exists
    boolean existsByChassisNo(String chassisNo);

    boolean existsByLicensePlate(String licensePlate);

    // Retrieve a car by its chassis number
    CarInfo findByChassisNo(String chassisNo);

    Optional<CarInfo> findByLicensePlate(String licensePlate);

    List<CarInfo> findByLicensePlateContainingIgnoreCase(String keyword);

}
