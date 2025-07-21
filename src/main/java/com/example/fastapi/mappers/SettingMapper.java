package com.example.fastapi.mappers;

import com.example.fastapi.dboModel.Setting;
import com.example.fastapi.dto.SettingRequestDTO;
import com.example.fastapi.dto.SettingResponseDTO;
import org.bson.types.ObjectId;

import java.util.Date;

public class SettingMapper {

    // تبدیل از Entity به ResponseDTO
    public static SettingResponseDTO toResponseDTO(Setting entity) {
        if (entity == null) return null;
        SettingResponseDTO dto = new SettingResponseDTO();
        dto.setId(entity.getId() != null ? entity.getId().toHexString() : null);
        dto.setStoreName(entity.getStoreName());
        dto.setInventoryEnabled(entity.isInventoryEnabled());
        dto.setCustomerEnabled(entity.isCustomerEnabled());
        dto.setDatabaseName(entity.getDatabaseName());
        return dto;
    }

    // تبدیل از RequestDTO به Entity (برای ایجاد یا آپدیت)
    public static Setting toEntity(SettingRequestDTO dto) {
        if (dto == null) return null;
        Setting entity = new Setting();

        // در اینجا فرض می‌کنیم که id در ریکوست نیست چون برای ساخت هست
        entity.setStoreName(dto.getStoreName());
        entity.setInventoryEnabled(dto.isInventoryEnabled());
        entity.setCustomerEnabled(dto.isCustomerEnabled());
        entity.setDatabaseName(dto.getDatabaseName());

        return entity;
    }

    // اگر لازم باشه میشه تبدیل از ResponseDTO به Entity و بالعکس هم اضافه کرد
}
