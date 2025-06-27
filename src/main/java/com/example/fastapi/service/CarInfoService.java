package com.example.fastapi.service;


import com.example.fastapi.dboModel.CarInfo;
import com.example.fastapi.repository.CarInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.FindAndModifyOptions;

import java.time.LocalDateTime;

@Service
public class CarInfoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    private final CarInfoRepository carInfoRepository;

    @Autowired
    public CarInfoService(CarInfoRepository carInfoRepository) {
        this.carInfoRepository = carInfoRepository;
    }

    // Insert a new car record into MongoDB
    public CarInfo insertCarInfo(CarInfo carInfo) {
        // Check if a car with this chassis number already exists
        if (carInfoRepository.existsByChassisNo(carInfo.getChassisNo())) {
            throw new IllegalArgumentException("Bu şasi numarasına sahip araç zaten mevcut");
        }

        // Check if a car with this license plate already exists
        if (carInfoRepository.existsByLicensePlate(carInfo.getLicensePlate())) {
            throw new IllegalArgumentException("Bu plakaya sahip araç zaten mevcut");
        }

        // Set current date and time if not provided
        if (carInfo.getDateTime() == null) {
            carInfo.setDateTime(LocalDateTime.now());
        }

        // Save the car information to MongoDB
        return carInfoRepository.save(carInfo);
    }


    public CarInfo getCarByLicensePlate(String licensePlate) {
        return carInfoRepository.findByLicensePlate(licensePlate);
    }

    public CarInfo updateCarInfoByLicensePlate(String licensePlate, CarInfo updatedCar) {
        Query query = new Query(Criteria.where("licensePlate").is(licensePlate));
        Update update = new Update()
                .set("brand", updatedCar.getBrand())
                .set("brandModel", updatedCar.getBrandModel())
                .set("chassisNo", updatedCar.getChassisNo())
                .set("motorNo", updatedCar.getMotorNo())
                .set("modelYear", updatedCar.getModelYear())
                .set("fuelType", updatedCar.getFuelType())
                .set("dateTime", updatedCar.getDateTime());

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        CarInfo updated = mongoTemplate.findAndModify(query, update, options, CarInfo.class);
        return updated;
    }


}
