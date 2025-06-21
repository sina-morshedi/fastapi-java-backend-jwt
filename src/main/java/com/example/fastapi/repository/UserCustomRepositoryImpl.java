package com.example.fastapi.repository;

import com.example.fastapi.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Repository;

import java.util.List;

// پیاده‌سازی UserCustomRepository با نام Impl برای شناسایی خودکار توسط Spring Data
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<UserProfileDTO> findUsersWithRolesAndPermissions() {
        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("roleId")
                .foreignField("_id")
                .as("role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("permissionId")
                .foreignField("_id")
                .as("permission");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupRole,
                Aggregation.unwind("role", true),
                lookupPermission,
                Aggregation.unwind("permission", true),
                Aggregation.project("firstName", "lastName")
                        .and("role.roleName").as("roleName")
                        .and("permission.permissionName").as("permissionName")
        );

        AggregationResults<UserProfileDTO> results = mongoTemplate.aggregate(
                aggregation, "users", UserProfileDTO.class);

        return results.getMappedResults();
    }
}
