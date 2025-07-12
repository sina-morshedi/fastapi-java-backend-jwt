package com.example.fastapi.service;

import com.example.fastapi.dto.TaskStatusCountDTO;
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
import java.util.Date;
import java.util.stream.Collectors;


import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.util.List;


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

    public List<CarRepairLogResponseDTO> getLatestRepairLogForEachCar() {
        return carRepairLogCustomRepositoryImpl.findLatestRepairLogForEachCar();
    }
    public List<CarRepairLogResponseDTO> getLatestRepairLogForEachCarAssignedToUser(String userId) {
        return carRepairLogCustomRepositoryImpl.findLatestRepairLogForEachCarAssignedToUser(userId);
    }

    public CarRepairLogResponseDTO getLatestLogsByLicensePlate(String licensePlate) {
        return carRepairLogCustomRepositoryImpl.findLatestCarRepairLogByLicensePlate(licensePlate);
    }
    public List<CarRepairLogResponseDTO> getLatestLogsByTaskStatusName(String taskStatusName) {
        return carRepairLogCustomRepositoryImpl.findLatestCarRepairLogsByTaskStatusName(taskStatusName);
    }

    public List<CarRepairLogResponseDTO> getLatestCarRepairLogsByTaskStatusNamesAndAssignedUserId(List<String> taskStatusNames, String assignedUserId) {
        return carRepairLogCustomRepositoryImpl.findLatestCarRepairLogsByTaskStatusNamesAndAssignedUserId(taskStatusNames,assignedUserId);
    }

    public List<CarRepairLogResponseDTO> getLogsByTaskStatusName(String taskStatusName) {
        return carRepairLogCustomRepositoryImpl.findCarRepairLogsByTaskStatusName(taskStatusName);
    }

    public CarRepairLogResponseDTO createLog(CarRepairLogRequestDTO requestDTO) {
        CarRepairLog entity = carRepairLogMapper.toEntity(requestDTO);
        entity.setDateTime(new Date());
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

    public CarRepairLogResponseDTO getLogById(String id) {
        CarRepairLogResponseDTO responseDTO = carRepairLogCustomRepositoryImpl.findCarRepairLogById(id);
        return responseDTO;
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
            carRepairLogMapper.updateEntityFromDTO(requestDTO, existingLog);
            existingLog.setDateTime(new Date());
            CarRepairLog updated = carRepairLogRepository.save(existingLog);
            return carRepairLogMapper.toResponseDTO(updated);
        });
    }

    public List<TaskStatusCountDTO> getCountCarsByLatestTaskStatus(){
        return carRepairLogCustomRepositoryImpl.countCarsByLatestTaskStatus();
    }

    public List<CarRepairLogResponseDTO> getCarRepairLogsByTaskNamesAndDateRange(
            List<String> taskStatusNames, Instant start, Instant end) {

        // تاریخ CutOff که از این تاریخ به بعد اطلاعات UTC ذخیره شدن
        final ZonedDateTime cutOffZoned = ZonedDateTime.of(2025, 7, 11, 17, 47, 0, 0, ZoneId.of("UTC"));

        // تبدیل Instant به ZonedDateTime با منطقه زمانی UTC
        ZonedDateTime startZdt = ZonedDateTime.ofInstant(start, ZoneId.of("UTC"));
        ZonedDateTime endZdt = ZonedDateTime.ofInstant(end, ZoneId.of("UTC"));

        // اصلاح تاریخ‌ها بر اساس CutOff
        if (startZdt.isBefore(cutOffZoned)) {
            // اگر قبل از CutOff است، تبدیل به ZonedDateTime با منطقه لوکال سرور (یا منطقه مورد نظر)
            ZonedDateTime localStartZdt = startZdt.withZoneSameInstant(ZoneId.systemDefault());
            start = localStartZdt.toInstant();
        }

        if (endZdt.isBefore(cutOffZoned)) {
            ZonedDateTime localEndZdt = endZdt.withZoneSameInstant(ZoneId.systemDefault());
            end = localEndZdt.toInstant();
        }

        // تبدیل Instant به Date (اگر لازم باشد، مثلا برای query دیتابیس)
        Date startDate = Date.from(start);
        Date endDate = Date.from(end);

        // فراخوانی متد ریپازیتوری با تاریخ‌های اصلاح شده
        return carRepairLogCustomRepositoryImpl.findCarRepairLogsByTaskNamesAndDateRange(
                taskStatusNames, startDate, endDate);
    }


    public List<CarRepairLogResponseDTO> getCarRepairLogsByLicensePlateAndTaskNames(
            String licensePlate, List<String> taskStatusNames){
        return carRepairLogCustomRepositoryImpl.findCarRepairLogsByLicensePlateAndTaskNames(
                licensePlate,taskStatusNames);
    }
}
