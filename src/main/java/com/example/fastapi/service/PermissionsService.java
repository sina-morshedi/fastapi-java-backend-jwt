package com.example.fastapi.service;

import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.repository.PermissionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PermissionsService {
    private final PermissionsRepository permissionsRepository;

    public PermissionsService(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    public Optional<Permissions> getByPermissionName(String permissionName) {
        Optional<Permissions> optional = permissionsRepository.findById(permissionName);
        return optional;
    }

//    public Users saveUser(Users user) {
//        return userRepository.save(user);
//    }

}
