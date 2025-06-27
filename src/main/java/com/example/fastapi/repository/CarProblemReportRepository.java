package com.example.fastapi.repository;

import com.example.fastapi.dboModel.CarProblemReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarProblemReportRepository extends MongoRepository<CarProblemReport, String> {
    List<CarProblemReport> findByCarId(String carId);
    List<CarProblemReport> findByCreatorUserId(String creatorUserId);
}
