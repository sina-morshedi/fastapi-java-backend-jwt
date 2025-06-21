package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Users;
import com.example.fastapi.dto.UserProfileDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends MongoRepository<Users, String>, UserCustomRepository {

    Optional<Users> findByFirstName(String firstName);

    Optional<Users> findById(String _id);
}

interface UserCustomRepository {
    List<UserProfileDTO> findUsersWithRolesAndPermissions();
}
