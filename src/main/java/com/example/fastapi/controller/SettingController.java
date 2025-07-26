package com.example.fastapi.controller;

import com.example.fastapi.dboModel.Setting;
import com.example.fastapi.service.SettingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/settings")
public class SettingController {

    private final SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping
    public ResponseEntity<Setting> createSetting(@RequestBody Setting setting) {
        Setting saved = settingService.saveSetting(setting);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Setting> getSettingById(@PathVariable String id) {
        Optional<Setting> setting = settingService.getSettingById(new ObjectId(id));
        return setting.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeName}")
    public ResponseEntity<Setting> getSettingByStoreName(@PathVariable String storeName) {
        Optional<Setting> setting = settingService.getSettingByStoreName(storeName);
        return setting.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Setting>> getAllSettings() {
        List<Setting> settings = settingService.getAllSettings();
        return ResponseEntity.ok(settings);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Setting> updateSetting(@PathVariable String id, @RequestBody Setting setting) {
        Optional<Setting> existing = settingService.getSettingById(new ObjectId(id));
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        setting.setId(new ObjectId(id));
        Setting updated = settingService.saveSetting(setting);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable String id) {
        settingService.deleteSettingById(new ObjectId(id));
        return ResponseEntity.noContent().build();
    }
}
