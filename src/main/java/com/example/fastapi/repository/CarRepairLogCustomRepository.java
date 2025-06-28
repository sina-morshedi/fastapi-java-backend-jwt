package com.example.fastapi.repository;

import com.example.fastapi.dto.CarRepairLogResponseDTO;

public interface CarRepairLogCustomRepository {
    CarRepairLogResponseDTO findCarRepairLogById(String id);
}
