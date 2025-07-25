package com.example.fastapi.service;

import com.example.fastapi.dboModel.Setting;
import com.example.fastapi.repository.SettingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fastapi.dto.SettingStatusDTO;

import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    private final SettingRepository settingRepository;


    @Autowired
    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public Setting saveSetting(Setting setting) {
        return settingRepository.save(setting);
    }

    public Optional<Setting> getSettingById(ObjectId id) {
        return settingRepository.findById(id);
    }

    public Optional<Setting> getSettingByStoreName(String storeName) {
        return settingRepository.findByStoreName(storeName);
    }

    public List<Setting> getAllSettings() {
        return settingRepository.findAll();
    }

    public void deleteSettingById(ObjectId id) {
        settingRepository.deleteById(id);
    }

    public Optional<SettingStatusDTO> getSettingStatusByStoreName(String storeName) {
        return settingRepository.findByStoreName(storeName)
                .map(s -> new SettingStatusDTO(s.isInventoryEnabled(), s.isCustomerEnabled()));
    }
    public void deleteSettingByStoreName(String storeName) {
        settingRepository.deleteByStoreName(storeName);
    }

    public boolean existsByStoreName(String storeName) {
        return settingRepository.existsByStoreName(storeName);
    }

    public List<Setting> getInventoryEnabledSettings() {
        return settingRepository.findByInventoryEnabledTrue();
    }

    public List<Setting> getCustomerEnabledSettings() {
        return settingRepository.findByCustomerEnabledTrue();
    }

    // متد جدید: چک کردن فعال بودن بخش انبار برای یک فروشگاه خاص
    public boolean isInventoryEnabled(String storeName) {
        Optional<Setting> setting = settingRepository.findByStoreName(storeName);
        return setting.map(Setting::isInventoryEnabled).orElse(false);
    }

    // متد جدید: چک کردن فعال بودن بخش مشتری برای یک فروشگاه خاص
    public boolean isCustomerEnabled(String storeName) {
        Optional<Setting> setting = settingRepository.findByStoreName(storeName);
        return setting.map(Setting::isCustomerEnabled).orElse(false);
    }

    // متد جدید: دریافت connection string برای storeName
    public Optional<String> getConnectionString(String storeName) {
        Optional<Setting> setting = settingRepository.findByStoreName(storeName);
        return setting.map(Setting::getConnectionString);
    }

    // متد جدید: گرفتن database name بر اساس storeName
    public Optional<String> getDatabaseName(String storeName) {
        Optional<Setting> setting = settingRepository.findByStoreName(storeName);
        return setting.map(Setting::getDatabaseName);
    }
}
