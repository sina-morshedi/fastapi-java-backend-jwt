package com.example.fastapi.mappers;

import com.example.fastapi.repository.CarInfoRepository;
import com.example.fastapi.repository.UsersRepository;
import com.example.fastapi.repository.RolesRepository;
import com.example.fastapi.repository.PermissionsRepository;
import com.example.fastapi.repository.TaskStatusRepository;
import com.example.fastapi.repository.CarProblemReportRepository;
import com.example.fastapi.dboModel.CarRepairLog;
import com.example.fastapi.dto.CarRepairLogRequestDTO;
import com.example.fastapi.dto.CarRepairLogResponseDTO;
import com.example.fastapi.dboModel.CarInfo;
import com.example.fastapi.dboModel.Users;
import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.dboModel.TaskStatus;
import com.example.fastapi.dboModel.CarProblemReport;
import com.example.fastapi.dto.CarInfoDTO;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.TaskStatusDTO;
import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.dto.PermissionDTO;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.CarProblemReportDTO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.types.ObjectId;


import com.example.fastapi.dboModel.CarInfo;

@Component
public class CarRepairLogMapper {

    @Autowired
    private CarInfoRepository carInfoRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    private RolesRepository rolesRepository;
    private PermissionsRepository permissionsRepository;

    @Autowired
    private CarProblemReportRepository carProblemReportRepository;
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

        // Fetch and map CarInfo
        CarInfo carInfo = carInfoRepository.findById(entity.getCarId()).orElse(null);
        if (carInfo != null) {
            CarInfoDTO carDto = new CarInfoDTO();
            carDto.setId(carInfo.getId());
            carDto.setChassisNo(carInfo.getChassisNo());
            carDto.setMotorNo(carInfo.getMotorNo());
            carDto.setLicensePlate(carInfo.getLicensePlate());
            carDto.setBrand(carInfo.getBrand());
            carDto.setBrandModel(carInfo.getBrandModel());
            carDto.setModelYear(carInfo.getModelYear());
            carDto.setFuelType(carInfo.getFuelType());
            carDto.setDateTime(carInfo.getDateTime());
            dto.setCar(carDto);
        } else {
            dto.setCar(null);
        }

        // Fetch and map Creator User
        Users user = userRepository.findById(entity.getCreatorUserId()).orElse(null);
        if (user != null) {
            UserProfileDTO userDto = new UserProfileDTO();
            userDto.setUserId(user.getId());
            userDto.setUsername(null); // چون توی Users نیست
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            RolesDTO roleDto = new RolesDTO();
            roleDto.setId(user.getRoleId());
            userDto.setRole(roleDto);
            PermissionDTO permissionDto = new PermissionDTO();
            permissionDto.setPermissionId(user.getPermissionId());
            userDto.setPermission(permissionDto);

        } else {
            dto.setCreatorUser(null);
        }

        // Set description
        dto.setDescription(entity.getDescription());

        // Fetch and map TaskStatus
        if (entity.getTaskStatusId() != null) {
            ObjectId objId = new ObjectId(entity.getTaskStatusId());
            TaskStatus status = taskStatusRepository.findById(objId).orElse(null);
            if (status != null) {
                TaskStatusDTO statusDto = new TaskStatusDTO();
                statusDto.setId(status.getId());
                statusDto.setTaskStatusName(status.getTaskStatusName());
                dto.setTaskStatus(statusDto);
            } else {
                dto.setTaskStatus(null);
            }
        } else {
            dto.setTaskStatus(null);
        }

        // Set DateTime
        dto.setDateTime(entity.getDateTime());

        // Fetch and map ProblemReport
        if (entity.getProblemReportId() != null) {
            CarProblemReport report = carProblemReportRepository.findById(entity.getProblemReportId()).orElse(null);
            if (report != null) {
                CarProblemReportDTO reportDto = new CarProblemReportDTO();
                reportDto.setId(report.getId());
                reportDto.setCarInfo(null);
                reportDto.setCreatorUser(null);
                reportDto.setProblemSummary(report.getProblemSummary());
                reportDto.setDateTime(report.getDateTime());
                dto.setProblemReport(reportDto);
            } else {
                dto.setProblemReport(null);
            }
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
