package com.example.fastapi.repository;

import com.example.fastapi.dto.UserProfileDTO;

public interface UserCustomRepository {
    UserProfileDTO findUserProfileByUserId(String userId);
}
