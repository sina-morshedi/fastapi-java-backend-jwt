package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Permissions;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PermissionsRepository extends MongoRepository<Permissions, ObjectId> {

    Optional<Permissions> findByPermissionName(String permissionName);
}
