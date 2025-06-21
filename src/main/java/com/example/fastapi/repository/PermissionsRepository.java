package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.dboModel.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PermissionsRepository extends MongoRepository<Permissions, String> {

    Optional<Permissions> findByPermissionName(String permissionName);
    Optional<Permissions> findById(String _id);
}
