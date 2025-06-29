package com.example.fastapi.service;

import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.dto.CarRepairLogResponseDTO;
import com.example.fastapi.repository.CarRepairLogCustomRepositoryImpl;
import com.example.fastapi.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private RolesRepository userRoleRepository;
    private CarRepairLogCustomRepositoryImpl carRepairLogCustomRepositoryImpl;

    public TestService(
            RolesRepository userRoleRepository,
            CarRepairLogCustomRepositoryImpl carRepairLogCustomRepositoryImpl
    ) {
        this.userRoleRepository = userRoleRepository;
        this.carRepairLogCustomRepositoryImpl = carRepairLogCustomRepositoryImpl;
    }

    public List<CarRepairLogResponseDTO> testService(String request)
    {

        return carRepairLogCustomRepositoryImpl.findCarRepairLogsByTaskStatusName(request);
    }
}
