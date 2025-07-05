package com.example.fastapi.mappers;

import com.example.fastapi.repository.*;
import com.example.fastapi.dboModel.*;
import com.example.fastapi.dto.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CarRepairLogMapper {

    @Autowired
    private CarInfoRepository carInfoRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Autowired
    private UserPassRepository userPassRepository;

    @Autowired
    private CarProblemReportRepository carProblemReportRepository;

    // تبدیل DTO به انتیتی (برای Create یا Update)
    public CarRepairLog toEntity(CarRepairLogRequestDTO dto) {
        if (dto == null) return null;

        CarRepairLog entity = new CarRepairLog();
        entity.setCarId(dto.getCarId());
        entity.setCreatorUserId(dto.getCreatorUserId());
        entity.setAssignedUserId(dto.getAssignedUserId());
        entity.setDescription(dto.getDescription());
        entity.setTaskStatusId(dto.getTaskStatusId());
        entity.setDateTime(dto.getDateTime());
        entity.setProblemReportId(dto.getProblemReportId());

        // تبدیل partsUsed از DTO به Entity
        if (dto.getPartsUsed() != null) {
            entity.setPartsUsed(dto.getPartsUsed().stream()
                    .map(partDto -> new PartUsed(
                            partDto.getPartName(),
                            partDto.getPartPrice(),
                            partDto.getQuantity()
                    )).collect(Collectors.toList())
            );
        } else {
            entity.setPartsUsed(new ArrayList<>());
        }

        return entity;
    }

    // تبدیل انتیتی به Response DTO (پاسخ)
    public CarRepairLogResponseDTO toResponseDTO(CarRepairLog entity) {
        if (entity == null) return null;

        CarRepairLogResponseDTO dto = new CarRepairLogResponseDTO();
        dto.setId(entity.getId());

        // در صورت نیاز فیلدهای دیگر را مقداردهی کن

        return dto;
    }

    // آپدیت انتیتی با داده‌های DTO (برای Update)
    public void updateEntityFromDTO(CarRepairLogRequestDTO dto, CarRepairLog entity) {
        if (dto == null || entity == null) return;

        entity.setCarId(dto.getCarId());
        entity.setCreatorUserId(dto.getCreatorUserId());
        entity.setDescription(dto.getDescription());
        entity.setTaskStatusId(dto.getTaskStatusId());
        entity.setDateTime(dto.getDateTime());
        entity.setProblemReportId(dto.getProblemReportId());

        if (dto.getPartsUsed() != null) {
            entity.setPartsUsed(dto.getPartsUsed().stream()
                    .map(partDto -> new PartUsed(
                            partDto.getPartName(),
                            partDto.getPartPrice(),
                            partDto.getQuantity()
                    )).collect(Collectors.toList())
            );
        } else {
            entity.setPartsUsed(new ArrayList<>());
        }
    }

    // توابع کمکی (مثلاً برای گرفتن DTO های تو در تو از دیتابیس)
    private PermissionDTO GetPermisionsDto(String permissionId){
        ObjectId objId = new ObjectId(permissionId);
        Permissions permission = permissionsRepository.findById(objId).orElse(null);

        PermissionDTO pDto = new PermissionDTO();
        if(permission != null) {
            pDto.setId(permission.getId());
            pDto.setPermissionName(permission.getPermissionName());
        }
        return pDto;
    }

    private RolesDTO GetRolesDto(String RoleId){
        ObjectId objId = new ObjectId(RoleId);
        Roles role = rolesRepository.findById(objId).orElse(null);

        RolesDTO rDto = new RolesDTO();
        if(role != null) {
            rDto.setId(role.getId());
            rDto.setRoleName(role.getRoleName());
        }
        return rDto;
    }

    private UserProfileDTO GetUserDTO(String userid) {
        Users user = userRepository.findById(userid).orElse(null);

        if (user != null) {
            UserPass userPass = userPassRepository.findByUserId(new ObjectId(user.getId())).orElse(null);
            UserProfileDTO userDto = new UserProfileDTO();
            userDto.setUserId(user.getId());
            if(userPass != null)
                userDto.setUsername(userPass.getUsername());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());

            RolesDTO roleDto = new RolesDTO();
            roleDto.setId(user.getRoleId());
            roleDto.setRoleName(GetRolesDto(user.getRoleId()).getRoleName());
            userDto.setRole(roleDto);

            PermissionDTO permissionDto = new PermissionDTO();
            permissionDto.setId(user.getPermissionId());
            permissionDto.setPermissionName(GetPermisionsDto(user.getPermissionId()).getPermissionName());
            userDto.setPermission(permissionDto);

            return userDto;
        } else {
            return null;
        }
    }

    private CarInfoDTO GetCarInfoDto(String carId){
        CarInfo carInfo = carInfoRepository.findById(carId).orElse(null);
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
            return carDto;
        }
        return null;
    }

}
