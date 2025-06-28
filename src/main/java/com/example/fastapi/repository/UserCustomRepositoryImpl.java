package com.example.fastapi.repository;
import com.example.fastapi.dto.PermissionDTO;
import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.dto.UserProfileDTO;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final MongoTemplate mongoTemplate;

    public UserCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserProfileDTO findUserProfileByUserId(String userId) {
        AggregationOperation matchUserPass = context -> new Document("$match",
                new Document("user_id", userId));

        AggregationOperation addUserIdObject = context -> new Document("$addFields",
                new Document("userIdObject", new Document("$toObjectId", "$user_id")));

        LookupOperation lookupUsers = LookupOperation.newLookup()
                .from("users")
                .localField("userIdObject")
                .foreignField("_id")
                .as("user");

        UnwindOperation unwindUser = Aggregation.unwind("user", true);

        AggregationOperation addRoleAndPermissionObjectIds = context -> new Document("$addFields", new Document()
                .append("roleIdObject", new Document("$toObjectId", "$user.roleId"))
                .append("permissionIdObject", new Document("$toObjectId", "$user.permissionId"))
        );

        LookupOperation lookupRoles = LookupOperation.newLookup()
                .from("roles")
                .localField("roleIdObject")
                .foreignField("_id")
                .as("role");

        UnwindOperation unwindRole = Aggregation.unwind("role", true);

        LookupOperation lookupPermissions = LookupOperation.newLookup()
                .from("permissions")
                .localField("permissionIdObject")
                .foreignField("_id")
                .as("permission");

        UnwindOperation unwindPermission = Aggregation.unwind("permission", true);

        ProjectionOperation project = Aggregation.project()
                .andExpression("user._id").as("userId")
                .andExpression("username").as("username")
                .and("user.firstName").as("firstName")
                .and("user.lastName").as("lastName")
                .and("role").as("role")
                .and("permission").as("permission");

        Aggregation aggregation = Aggregation.newAggregation(
                matchUserPass,
                addUserIdObject,
                lookupUsers,
                unwindUser,
                addRoleAndPermissionObjectIds,
                lookupRoles,
                unwindRole,
                lookupPermissions,
                unwindPermission,
                project
        );

        AggregationResults<UserProfileDTO> results = mongoTemplate.aggregate(
                aggregation,
                "userPass",
                UserProfileDTO.class
        );
        System.out.println(results.getMappedResults());

        return results.getUniqueMappedResult();
    }

}
