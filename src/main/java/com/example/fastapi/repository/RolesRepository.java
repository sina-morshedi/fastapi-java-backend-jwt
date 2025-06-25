package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Roles;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RolesRepository extends MongoRepository<Roles, ObjectId> {

    Optional<Roles> findByRoleName(String roleName);
}
