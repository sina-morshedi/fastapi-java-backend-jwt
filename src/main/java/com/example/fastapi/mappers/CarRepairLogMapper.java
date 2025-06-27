package com.example.fastapi.mappers;

import com.example.fastapi.dboModel.CarRepairLog;
import com.example.fastapi.dto.CarRepairLogRequestDTO;
import com.example.fastapi.dto.CarRepairLogResponseDTO;
import com.example.fastapi.dto.CarInfoDTO;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.TaskStatusDTO;
import com.example.fastapi.dto.CarProblemReportDTO;
import org.springframework.stereotype.Component;

@Component
public class CarRepairLogMapper {

    // Convert Request DTO to Entity (DBO)
    public CarRepairLog toEntity(CarRepairLogRequestDTO dto) {
        if (dto == null) return null;

        CarRepairLog entity = new CarRepairLog();
        entity.setCarId(dto.getCarId());
        entity.setCreatorUserId(dto.getCreatorUserId());
        entity.setDescription(dto.getDescription());
        entity.setTaskStatusId(dto.getTaskStatusId());
        entity.setDateTime(dto.getDateTime());
        entity.setProblemReportId(dto.getProblemReportId());

        // departmentId and carRequiredDepartmentId حذف شدند چون در ریکوئست نبودند

        return entity;
    }

    // Convert Entity (DBO) to Response DTO
    public CarRepairLogResponseDTO toResponseDTO(CarRepairLog entity) {
        if (entity == null) return null;

        CarRepairLogResponseDTO dto = new CarRepairLogResponseDTO();

        dto.setId(entity.getId());

        CarInfoDTO carDto = new CarInfoDTO();
        carDto.setId(entity.getCarId());
        dto.setCar(carDto);

        UserProfileDTO userDto = new UserProfileDTO();
        userDto.setUserId(entity.getCreatorUserId());
        dto.setCreatorUser(userDto);


        dto.setDescription(entity.getDescription());

        if (entity.getTaskStatusId() != null) {
            TaskStatusDTO statusDto = new TaskStatusDTO();
            statusDto.setId(entity.getTaskStatusId());
            dto.setTaskStatus(statusDto);
        } else {
            dto.setTaskStatus(null);
        }

        dto.setDateTime(entity.getDateTime());

        if (entity.getProblemReportId() != null) {
            CarProblemReportDTO reportDto = new CarProblemReportDTO();
            reportDto.setId(entity.getProblemReportId());
            dto.setProblemReport(reportDto);

        } else {
            dto.setProblemReport(null);
        }

        return dto;
    }

    // Update existing Entity from Request DTO (useful in update)
    public void updateEntityFromDTO(CarRepairLogRequestDTO dto, CarRepairLog entity) {
        if (dto == null || entity == null) return;

        entity.setCarId(dto.getCarId());
        entity.setCreatorUserId(dto.getCreatorUserId());
        entity.setDescription(dto.getDescription());
        entity.setTaskStatusId(dto.getTaskStatusId());
        entity.setDateTime(dto.getDateTime());
        entity.setProblemReportId(dto.getProblemReportId());

    }
}
