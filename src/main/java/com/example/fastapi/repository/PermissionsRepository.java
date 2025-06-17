package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Permissions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PermissionsRepository extends MongoRepository<Permissions, String> {

    List<Permissions> findByPermissionName(String permissionName);
//    List<Users> findByRoleId(String roleId);
}
