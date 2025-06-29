package com.example.fastapi.service;

import com.example.fastapi.mappers.CarRepairLogMapper;
import com.example.fastapi.dto.CarRepairLogResponseDTO;
import com.example.fastapi.dto.CarRepairLogRequestDTO;
import com.example.fastapi.dboModel.CarRepairLog;
import com.example.fastapi.repository.CarRepairLogRepository;
import com.example.fastapi.repository.CarRepairLogCustomRepositoryImpl;
import com.example.fastapi.repository.CarInfoRepository;
import com.example.fastapi.dboModel.CarInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.stream.Collectors;



import java.util.List;
import java.util.Optional;

@Service
public class CarRepairLogService {

    @Autowired
    private CarRepairLogRepository carRepairLogRepository;

    @Autowired
    private CarInfoRepository carInfoRepository;

    @Autowired
    private CarRepairLogMapper carRepairLogMapper;
    @Autowired
    private CarRepairLogCustomRepositoryImpl carRepairLogCustomRepositoryImpl;


    public List<CarRepairLogResponseDTO> getLogsByLicensePlate(String licensePlate) {
        return carRepairLogCustomRepositoryImpl.findCarRepairLogsByLicensePlate(licensePlate);
    }

    public List<CarRepairLogResponseDTO> getLogsByTaskStatusName(String taskStatusName) {
        return carRepairLogCustomRepositoryImpl.findCarRepairLogsByTaskStatusName(taskStatusName);
    }

    public CarRepairLogResponseDTO createLog(CarRepairLogRequestDTO requestDTO) {
        CarRepairLog entity = carRepairLogMapper.toEntity(requestDTO);
        CarRepairLog saved = carRepairLogRepository.save(entity);
        CarRepairLogResponseDTO responseDTO = carRepairLogCustomRepositoryImpl.findCarRepairLogById(saved.getId());
        return responseDTO;
    }

    public List<CarRepairLogResponseDTO> getAllLogs() {
        List<CarRepairLog> allLogs = carRepairLogRepository.findAll();
        return allLogs.stream()
                .map(carRepairLogMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<CarRepairLogResponseDTO> getLogById(String id) {
        return carRepairLogRepository.findById(id)
                .map(carRepairLogMapper::toResponseDTO);
    }

    public boolean deleteLog(String id) {
        if (carRepairLogRepository.existsById(id)) {
            carRepairLogRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<CarRepairLogResponseDTO> updateLog(String id, CarRepairLogRequestDTO requestDTO) {
        return carRepairLogRepository.findById(id).map(existingLog -> {
            // map updated fields from requestDTO to existingLog entity
            carRepairLogMapper.updateEntityFromDTO(requestDTO, existingLog);
            CarRepairLog updated = carRepairLogRepository.save(existingLog);
            return carRepairLogMapper.toResponseDTO(updated);
        });
    }
}
