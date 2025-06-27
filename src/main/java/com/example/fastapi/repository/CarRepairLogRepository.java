package com.example.fastapi.repository;

import com.example.fastapi.dboModel.CarRepairLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepairLogRepository extends MongoRepository<CarRepairLog, String> {
    List<CarRepairLog> findByCarId(String carId);
    List<CarRepairLog> findByCreatorUserId(String creatorUserId);
    List<CarRepairLog> findByTaskStatusId(String taskStatusId);
    List<CarRepairLog> findByProblemReportId(String problemReportId);
}
