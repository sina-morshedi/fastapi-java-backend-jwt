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
    public  UserProfileDTO findUserProfileByUserId(String userId) {

        MatchOperation matchUserPass = Aggregation.match(Criteria.where("user_id").is(userId));


        LookupOperation lookupUsers = LookupOperation.newLookup()
                .from("users")
                .localField("user_id")
                .foreignField("_id")
                .as("user");


        UnwindOperation unwindUser = Aggregation.unwind("user", true);


        AggregationOperation addRoleObjectId = context -> new Document("$addFields",
                new Document("user.roleObjectId", new Document("$toObjectId", "$user.roleId")));

        AggregationOperation addPermissionObjectId = context -> new Document("$addFields",
                new Document("user.permissionObjectId", new Document("$toObjectId", "$user.permissionId")));


        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("user.roleObjectId")
                .foreignField("_id")
                .as("role");

        UnwindOperation unwindRole = Aggregation.unwind("role", true);

        // lookup برای permissions بر اساس permissionObjectId جدید
        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("user.permissionObjectId")
                .foreignField("_id")
                .as("permission");

        UnwindOperation unwindPermission = Aggregation.unwind("permission", true);

        ProjectionOperation project = Aggregation.project()
                .andExpression("user_id").as("userId")
                .and("username").as("username")
                .and("user.firstName").as("firstName")
                .and("user.lastName").as("lastName")
                .and("role._id").as("roleId")
                .and("role.roleName").as("roleName")
                .and("permission._id").as("permissionId")
                .and("permission.permissionName").as("permissionName");



        Aggregation aggregation = Aggregation.newAggregation(
                matchUserPass,
                lookupUsers,
                unwindUser,
                addRoleObjectId,
                addPermissionObjectId,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                project
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "userPass", Document.class);
        List<Document> mappedResults = results.getMappedResults();

        if (mappedResults.isEmpty()) {
            return null;
        }

        Document doc = mappedResults.get(0);

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(doc.getString("userId"));
        dto.setUsername(doc.getString("username"));
        dto.setFirstName(doc.getString("firstName"));
        dto.setLastName(doc.getString("lastName"));

        String roleId = doc.getString("roleId");
        String roleName = doc.getString("roleName");
        if (roleId != null || roleName != null) {
            RolesDTO role = new RolesDTO();
            role.setId(roleId);
            role.setRoleName(roleName);
            dto.setRole(role);
        }

        String permissionId = doc.getString("permissionId");
        String permissionName = doc.getString("permissionName");
        if (permissionId != null || permissionName != null) {
            PermissionDTO permission = new PermissionDTO();
            permission.setPermissionId(permissionId);
            permission.setPermissionName(permissionName);
            dto.setPermission(permission);
        }



        return dto;
    }
}
