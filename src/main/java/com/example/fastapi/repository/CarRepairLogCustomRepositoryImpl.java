package com.example.fastapi.repository;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.example.fastapi.dto.CarRepairLogResponseDTO;


@Repository
public class CarRepairLogCustomRepositoryImpl implements CarRepairLogCustomRepository {

    private final MongoTemplate mongoTemplate;

    public CarRepairLogCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CarRepairLogResponseDTO findCarRepairLogById(String id) {
        MatchOperation matchById = Aggregation.match(Criteria.where("_id").is(id));

        AggregationOperation addFields = context -> new Document("$addFields",
                new Document("carIdObject", new Document("$toObjectId", "$carId"))
                        .append("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
                        .append("problemReportIdObject", new Document("$toObjectId", "$problemReportId"))
        );

        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carIdObject")
                .foreignField("_id")
                .as("car");

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");

        UnwindOperation unwindCar = Aggregation.unwind("car", true);
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);

        AggregationOperation addRoleIdObject = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
        );

        AggregationOperation addPermissionIdObject = context -> new Document("$addFields",
                new Document("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");

        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addTaskStatusIdObject = context -> new Document("$addFields",
                new Document("taskStatusIdObject", new Document("$toObjectId", "$taskStatusId"))
        );

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusIdObject")
                .foreignField("_id")
                .as("taskStatus");

        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        AggregationOperation addProblemReportObjectId = context -> new Document("$addFields",
                new Document("problemReportIdObject", new Document("$toObjectId", "$problemReportId"))
        );

        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportIdObject")
                .foreignField("_id")
                .as("problemReport");

        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);

        AggregationOperation addNestedFields = context -> new Document("$addFields",
                new Document("problemReport.carIdObject", new Document("$toObjectId", "$problemReport.carId"))
                        .append("problemReport.creatorUserIdObject", new Document("$toObjectId", "$problemReport.creatorUserId"))
        );

        LookupOperation lookupProblemReportCar = LookupOperation.newLookup()
                .from("carInfo")
                .localField("problemReport.carIdObject")
                .foreignField("_id")
                .as("problemReport.carInfo");

        LookupOperation lookupProblemReportCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("problemReport.creatorUserIdObject")
                .foreignField("_id")
                .as("problemReport.creatorUser");

        UnwindOperation unwindProblemReportCar = Aggregation.unwind("problemReport.carInfo", true);
        UnwindOperation unwindProblemReportCreatorUser = Aggregation.unwind("problemReport.creatorUser", true);

        AggregationOperation addProblemReportCreatorUserRoleIdObject = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
        );

        AggregationOperation addProblemReportCreatorUserPermissionIdObject = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId"))
        );

        LookupOperation lookupProblemReportCreatorUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("problemReport.creatorUser.roleIdObject")
                .foreignField("_id")
                .as("problemReport.creatorUser.role");

        LookupOperation lookupProblemReportCreatorUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("problemReport.creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("problemReport.creatorUser.permission");

        UnwindOperation unwindProblemReportCreatorUserRole = Aggregation.unwind("problemReport.creatorUser.role", true);
        UnwindOperation unwindProblemReportCreatorUserPermission = Aggregation.unwind("problemReport.creatorUser.permission", true);


        ProjectionOperation project = Aggregation.project()
                .andExpression("_id").as("id")
                .and("car").as("car")
                .and("creatorUser._id").as("creatorUser.userId")  // map ObjectId to userId string
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("description").as("description")
                .and("dateTime").as("dateTime")
                .and("taskStatusId").as("taskStatusId")
                .and("taskStatus").as("taskStatus")
                .and("problemReport._id").as("problemReport.id")
                .and("problemReport.carInfo").as("problemReport.carInfo")
                .and("problemReport.creatorUser._id").as("problemReport.creatorUser.userId")  // map ObjectId to userId string
                .and("problemReport.creatorUser.username").as("problemReport.creatorUser.username")
                .and("problemReport.creatorUser.firstName").as("problemReport.creatorUser.firstName")
                .and("problemReport.creatorUser.lastName").as("problemReport.creatorUser.lastName")
                .and("problemReport.creatorUser.role").as("problemReport.creatorUser.role")
                .and("problemReport.creatorUser.permission").as("problemReport.creatorUser.permission")
                .and("problemReport.problemSummary").as("problemReport.problemSummary")
                .and("problemReport.dateTime").as("problemReport.dateTime")
                ;

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(id)),
                addFields,
                lookupCarInfo,
                unwindCar,
                lookupCreatorUser,
                unwindCreatorUser,
                addRoleIdObject,
                addPermissionIdObject,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addTaskStatusIdObject,   // 👈 اضافه شد
                lookupTaskStatus,        // 👈 اضافه شد
                unwindTaskStatus,
                addProblemReportObjectId,
                lookupProblemReport,
                unwindProblemReport,
                addNestedFields,
                lookupProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCar,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserRoleIdObject,
                addProblemReportCreatorUserPermissionIdObject,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getUniqueMappedResult();

    }

}
