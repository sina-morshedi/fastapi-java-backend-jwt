package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.dboModel.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;


@Repository
public interface RolesRepository extends MongoRepository<Roles, String> {

    Optional<Roles> findByRoleName(String roleName);
    Optional<Roles> findById(String _id);
}

