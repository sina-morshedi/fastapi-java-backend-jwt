package com.example.fastapi.repository;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;



import com.example.fastapi.dto.CarRepairLogResponseDTO;
import java.util.Arrays;
import java.util.List;


@Repository
public class CarRepairLogCustomRepositoryImpl implements CarRepairLogCustomRepository {

    private final MongoTemplate mongoTemplate;

    public CarRepairLogCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public CarRepairLogResponseDTO findCarRepairLogById(String id) {
        MatchOperation matchById = Aggregation.match(Criteria.where("_id").is(new ObjectId(id)));

        AggregationOperation addFields = context -> new Document("$addFields",
                new Document("carIdObject", new Document("$toObjectId", "$carId"))
                        .append("creatorUserIdObject", new Document("$toObjectId", "$creatorUserId"))
                        .append("taskStatusIdObject", new Document("$toObjectId", "$taskStatusId"))
                        .append("problemReportIdObject", new Document("$toObjectId", "$problemReportId"))
                        .append("assignedUserIdObject", new Document("$toObjectId", "$assignedUserId"))
        );

        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carIdObject")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserIdObject")
                .foreignField("_id")
                .as("creatorUser");
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);

        AggregationOperation addRoleIdObject = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");
        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusIdObject")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        LookupOperation lookupAssignedUser = LookupOperation.newLookup()
                .from("users")
                .localField("assignedUserIdObject")
                .foreignField("_id")
                .as("assignedUser");
        UnwindOperation unwindAssignedUser = Aggregation.unwind("assignedUser", true);

        AggregationOperation addAssignedUserFields = context -> new Document("$addFields",
                new Document("assignedUser.roleIdObject", new Document("$toObjectId", "$assignedUser.roleId"))
                        .append("assignedUser.permissionIdObject", new Document("$toObjectId", "$assignedUser.permissionId"))
        );

        LookupOperation lookupAssignedUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("assignedUser.roleIdObject")
                .foreignField("_id")
                .as("assignedUser.role");
        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);

        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");
        UnwindOperation unwindAssignedUserPermission = Aggregation.unwind("assignedUser.permission", true);

        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportIdObject")
                .foreignField("_id")
                .as("problemReport");
        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);

        AggregationOperation addNestedProblemFields = context -> new Document("$addFields",
                new Document("problemReport.carIdObject", new Document("$toObjectId", "$problemReport.carId"))
                        .append("problemReport.creatorUserIdObject", new Document("$toObjectId", "$problemReport.creatorUserId")));

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

        AggregationOperation addProblemReportCreatorUserFields = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
                        .append("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId")));

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

        AggregationOperation convertProblemReportCreatorUserIdToString = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.userId", new Document("$toString", "$problemReport.creatorUser._id")));

        ProjectionOperation project = Aggregation.project()
                .and("_id").as("id")
                .and("car").as("carInfo")
                .and("creatorUser._id").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("assignedUser._id").as("assignedUser.userId")
                .and("assignedUser.username").as("assignedUser.username")
                .and("assignedUser.firstName").as("assignedUser.firstName")
                .and("assignedUser.lastName").as("assignedUser.lastName")
                .and("assignedUser.role").as("assignedUser.role")
                .and("assignedUser.permission").as("assignedUser.permission")
                .and("description").as("description")
                .and("taskStatus").as("taskStatus")
                .and("dateTime").as("dateTime")
                .and("problemReport").as("problemReport");

        Aggregation aggregation = Aggregation.newAggregation(
                matchById,
                addFields,
                lookupCarInfo,
                unwindCar,
                lookupCreatorUser,
                unwindCreatorUser,
                addRoleIdObject,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                lookupTaskStatus,
                unwindTaskStatus,
                lookupAssignedUser,
                unwindAssignedUser,
                addAssignedUserFields,
                lookupAssignedUserRole,
                unwindAssignedUserRole,
                lookupAssignedUserPermission,
                unwindAssignedUserPermission,
                lookupProblemReport,
                unwindProblemReport,
                addNestedProblemFields,
                lookupProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCar,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getUniqueMappedResult();
    }


    public List<CarRepairLogResponseDTO> findCarRepairLogsByLicensePlate(String licensePlate) {
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");

        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        MatchOperation matchByLicensePlate = Aggregation.match(Criteria.where("car.licensePlate").is(licensePlate));

        // creatorUser
        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserId")
                .foreignField("_id")
                .as("creatorUser");

        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);

        AggregationOperation addRoleIdObject = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
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

        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject",
                        new Document("$cond",
                                new Document("if", new Document("$and", Arrays.asList(
                                        new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                        new Document("$ne", Arrays.asList("$assignedUserId", ""))
                                )))
                                        .append("then", new Document("$toObjectId", "$assignedUserId"))
                                        .append("else", null)
                        )
                )
        );

        LookupOperation lookupAssignedUser = LookupOperation.newLookup()
                .from("users")
                .localField("assignedUserIdObject")
                .foreignField("_id")
                .as("assignedUser");

        UnwindOperation unwindAssignedUser = Aggregation.unwind("assignedUser", true);

        AggregationOperation addAssignedUserRolePermissionIdObjects = context -> new Document("$addFields",
                new Document("assignedUser.roleIdObject", new Document("$toObjectId", "$assignedUser.roleId"))
                        .append("assignedUser.permissionIdObject", new Document("$toObjectId", "$assignedUser.permissionId"))
        );

        LookupOperation lookupAssignedUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("assignedUser.roleIdObject")
                .foreignField("_id")
                .as("assignedUser.role");

        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");

        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);
        UnwindOperation unwindAssignedUserPermission = Aggregation.unwind("assignedUser.permission", true);

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");

        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportId")
                .foreignField("_id")
                .as("problemReport");

        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);

        AggregationOperation addNestedProblemFields = context -> new Document("$addFields",
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

        AggregationOperation addProblemReportCreatorUserFields = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
                        .append("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId"))
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

        AggregationOperation convertProblemReportCreatorUserIdToString = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.userId", new Document("$toString", "$problemReport.creatorUser._id"))
        );

        // اضافه کردن partsUsed به aggregation
        UnwindOperation unwindPartsUsed = Aggregation.unwind("partsUsed", true);

        ProjectionOperation project = Aggregation.project()
                .and("_id").as("id")
                .and("car").as("carInfo")
                .and("creatorUser._id").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("assignedUser._id").as("assignedUser.userId")
                .and("assignedUser.username").as("assignedUser.username")
                .and("assignedUser.firstName").as("assignedUser.firstName")
                .and("assignedUser.lastName").as("assignedUser.lastName")
                .and("assignedUser.role").as("assignedUser.role")
                .and("assignedUser.permission").as("assignedUser.permission")
                .and("description").as("description")
                .and("taskStatus").as("taskStatus")
                .and("dateTime").as("dateTime")
                .and("problemReport").as("problemReport")
                .and("partsUsed").as("partsUsed");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                matchByLicensePlate,
                lookupCreatorUser,
                unwindCreatorUser,
                addRoleIdObject,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addAssignedUserIdObject,
                lookupAssignedUser,
                unwindAssignedUser,
                addAssignedUserRolePermissionIdObjects,
                lookupAssignedUserRole,
                unwindAssignedUserRole,
                lookupAssignedUserPermission,
                unwindAssignedUserPermission,
                lookupTaskStatus,
                unwindTaskStatus,
                lookupProblemReport,
                unwindProblemReport,
                addNestedProblemFields,
                lookupProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCar,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                unwindPartsUsed,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getMappedResults();
    }

    public CarRepairLogResponseDTO findLatestCarRepairLogByLicensePlate(String licensePlate) {
        // lookup به carInfo و match روی پلاک
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");

        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        MatchOperation matchByLicensePlate = Aggregation.match(Criteria.where("car.licensePlate").is(licensePlate));

        // مرتب‌سازی نزولی تاریخ و محدود کردن به ۱ رکورد
        SortOperation sortByDateDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));
        LimitOperation limitToOne = Aggregation.limit(1);

        // lookup برای creatorUser
        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserId")
                .foreignField("_id")
                .as("creatorUser");
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);

        AggregationOperation addRoleIdObject = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
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

        // lookup برای assignedUser (کاربر اختصاص داده شده)
        LookupOperation lookupAssignedUser = LookupOperation.newLookup()
                .from("users")
                .localField("assignedUserId")
                .foreignField("_id")
                .as("assignedUser");
        UnwindOperation unwindAssignedUser = Aggregation.unwind("assignedUser", true);

        AggregationOperation addAssignedUserRolePermissionIds = context -> new Document("$addFields",
                new Document("assignedUser.roleIdObject", new Document("$toObjectId", "$assignedUser.roleId"))
                        .append("assignedUser.permissionIdObject", new Document("$toObjectId", "$assignedUser.permissionId"))
        );

        LookupOperation lookupAssignedUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("assignedUser.roleIdObject")
                .foreignField("_id")
                .as("assignedUser.role");

        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");

        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);
        UnwindOperation unwindAssignedUserPermission = Aggregation.unwind("assignedUser.permission", true);

        // lookup برای taskStatus
        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        // lookup برای problemReport
        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportId")
                .foreignField("_id")
                .as("problemReport");
        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);

        AggregationOperation addNestedProblemFields = context -> new Document("$addFields",
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

        AggregationOperation addProblemReportCreatorUserFields = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
                        .append("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId"))
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

        AggregationOperation convertProblemReportCreatorUserIdToString = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.userId", new Document("$toString", "$problemReport.creatorUser._id"))
        );

        // پروژه نهایی خروجی (اضافه شدن assignedUser)
        ProjectionOperation project = Aggregation.project()
                .and("_id").as("id")
                .and("car").as("carInfo")
                .and("creatorUser._id").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("assignedUser._id").as("assignedUser.userId")
                .and("assignedUser.username").as("assignedUser.username")
                .and("assignedUser.firstName").as("assignedUser.firstName")
                .and("assignedUser.lastName").as("assignedUser.lastName")
                .and("assignedUser.role").as("assignedUser.role")
                .and("assignedUser.permission").as("assignedUser.permission")
                .and("description").as("description")
                .and("taskStatus").as("taskStatus")
                .and("dateTime").as("dateTime")
                .and("problemReport").as("problemReport");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                matchByLicensePlate,
                sortByDateDesc,
                limitToOne,
                lookupCreatorUser,
                unwindCreatorUser,
                addRoleIdObject,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                lookupAssignedUser,
                unwindAssignedUser,
                addAssignedUserRolePermissionIds,
                lookupAssignedUserRole,
                unwindAssignedUserRole,
                lookupAssignedUserPermission,
                unwindAssignedUserPermission,
                lookupTaskStatus,
                unwindTaskStatus,
                lookupProblemReport,
                unwindProblemReport,
                addNestedProblemFields,
                lookupProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCar,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getUniqueMappedResult();
    }



    public List<CarRepairLogResponseDTO> findCarRepairLogsByTaskStatusName(String taskStatusName) {
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserId")
                .foreignField("_id")
                .as("creatorUser");
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);

        AggregationOperation addRoleAndPermissionIds = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
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

        // lookup برای assignedUser (کاربر اختصاص داده شده)
        LookupOperation lookupAssignedUser = LookupOperation.newLookup()
                .from("users")
                .localField("assignedUserId")
                .foreignField("_id")
                .as("assignedUser");
        UnwindOperation unwindAssignedUser = Aggregation.unwind("assignedUser", true); // true برای پرزرو کردن null

        AggregationOperation addAssignedUserRolePermissionIds = context -> new Document("$addFields",
                new Document("assignedUser.roleIdObject", new Document("$toObjectId", "$assignedUser.roleId"))
                        .append("assignedUser.permissionIdObject", new Document("$toObjectId", "$assignedUser.permissionId"))
        );

        LookupOperation lookupAssignedUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("assignedUser.roleIdObject")
                .foreignField("_id")
                .as("assignedUser.role");

        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");

        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);
        UnwindOperation unwindAssignedUserPermission = Aggregation.unwind("assignedUser.permission", true);

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        MatchOperation matchByTaskStatusName = Aggregation.match(
                Criteria.where("taskStatus.taskStatusName").is(taskStatusName)
        );

        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportId")
                .foreignField("_id")
                .as("problemReport");
        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);

        AggregationOperation addNestedProblemFields = context -> new Document("$addFields",
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

        AggregationOperation addProblemReportCreatorUserFields = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
                        .append("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId"))
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

        AggregationOperation convertProblemReportCreatorUserIdToString = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.userId", new Document("$toString", "$problemReport.creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .and("_id").as("id")
                .and("car").as("carInfo")
                .and("creatorUser._id").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("assignedUser._id").as("assignedUser.userId")
                .and("assignedUser.username").as("assignedUser.username")
                .and("assignedUser.firstName").as("assignedUser.firstName")
                .and("assignedUser.lastName").as("assignedUser.lastName")
                .and("assignedUser.role").as("assignedUser.role")
                .and("assignedUser.permission").as("assignedUser.permission")
                .and("description").as("description")
                .and("taskStatus").as("taskStatus")
                .and("dateTime").as("dateTime")
                .and("problemReport").as("problemReport");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                lookupCreatorUser,
                unwindCreatorUser,
                addRoleAndPermissionIds,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                lookupAssignedUser,
                unwindAssignedUser,
                addAssignedUserRolePermissionIds,
                lookupAssignedUserRole,
                unwindAssignedUserRole,
                lookupAssignedUserPermission,
                unwindAssignedUserPermission,
                lookupTaskStatus,
                unwindTaskStatus,
                matchByTaskStatusName,
                lookupProblemReport,
                unwindProblemReport,
                addNestedProblemFields,
                lookupProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCar,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getMappedResults();
    }


    public List<CarRepairLogResponseDTO> findLatestCarRepairLogsByTaskStatusName(String taskStatusName) {

        // Step 1: sort descending by dateTime
        SortOperation sortByDateTimeDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        // Step 2: group by carId to keep only latest log
        GroupOperation groupByCarId = Aggregation.group("carId")
                .first(Aggregation.ROOT).as("latestLog");

        // Step 3: unwrap latestLog to root
        ReplaceRootOperation replaceWithLatestLog = Aggregation.replaceRoot("latestLog");

        // Step 4: taskStatus lookup and match
        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);
        MatchOperation matchTaskStatusName = Aggregation.match(
                Criteria.where("taskStatus.taskStatusName").is(taskStatusName)
        );

        // Step 5: carInfo lookup
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("carInfo");
        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);

        // Step 6: creatorUser lookup + role/permission
        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserId")
                .foreignField("_id")
                .as("creatorUser");
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);

        AggregationOperation addCreatorUserFields = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
        );

        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");
        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        // Step 7: assignedUser lookup + role/permission (جدید)
        LookupOperation lookupAssignedUser = LookupOperation.newLookup()
                .from("users")
                .localField("assignedUserId")
                .foreignField("_id")
                .as("assignedUser");
        UnwindOperation unwindAssignedUser = Aggregation.unwind("assignedUser", true);

        AggregationOperation addAssignedUserFields = context -> new Document("$addFields",
                new Document("assignedUser.roleIdObject", new Document("$toObjectId", "$assignedUser.roleId"))
                        .append("assignedUser.permissionIdObject", new Document("$toObjectId", "$assignedUser.permissionId"))
        );

        LookupOperation lookupAssignedUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("assignedUser.roleIdObject")
                .foreignField("_id")
                .as("assignedUser.role");
        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);

        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");
        UnwindOperation unwindAssignedUserPermission = Aggregation.unwind("assignedUser.permission", true);

        // Step 8: problemReport lookup
        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportId")
                .foreignField("_id")
                .as("problemReport");
        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);

        AggregationOperation addProblemReportFields = context -> new Document("$addFields",
                new Document("problemReport.carIdObject", new Document("$toObjectId", "$problemReport.carId"))
                        .append("problemReport.creatorUserIdObject", new Document("$toObjectId", "$problemReport.creatorUserId"))
        );

        LookupOperation lookupProblemReportCar = LookupOperation.newLookup()
                .from("carInfo")
                .localField("problemReport.carIdObject")
                .foreignField("_id")
                .as("problemReport.carInfo");
        UnwindOperation unwindProblemReportCar = Aggregation.unwind("problemReport.carInfo", true);

        LookupOperation lookupProblemReportCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("problemReport.creatorUserIdObject")
                .foreignField("_id")
                .as("problemReport.creatorUser");
        UnwindOperation unwindProblemReportCreatorUser = Aggregation.unwind("problemReport.creatorUser", true);

        AggregationOperation addProblemReportCreatorUserFields = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
                        .append("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId"))
        );

        LookupOperation lookupProblemReportCreatorUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("problemReport.creatorUser.roleIdObject")
                .foreignField("_id")
                .as("problemReport.creatorUser.role");
        UnwindOperation unwindProblemReportCreatorUserRole = Aggregation.unwind("problemReport.creatorUser.role", true);

        LookupOperation lookupProblemReportCreatorUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("problemReport.creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("problemReport.creatorUser.permission");
        UnwindOperation unwindProblemReportCreatorUserPermission = Aggregation.unwind("problemReport.creatorUser.permission", true);

        AggregationOperation convertProblemReportCreatorUserIdToString = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.userId", new Document("$toString", "$problemReport.creatorUser._id"))
        );

        // Step 9: project output
        ProjectionOperation project = Aggregation.project()
                .and("_id").as("id")
                .and("carInfo").as("carInfo")
                .and("creatorUser._id").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("assignedUser._id").as("assignedUser.userId")
                .and("assignedUser.username").as("assignedUser.username")
                .and("assignedUser.firstName").as("assignedUser.firstName")
                .and("assignedUser.lastName").as("assignedUser.lastName")
                .and("assignedUser.role").as("assignedUser.role")
                .and("assignedUser.permission").as("assignedUser.permission")
                .and("description").as("description")
                .and("taskStatus").as("taskStatus")
                .and("dateTime").as("dateTime")
                .and("problemReport").as("problemReport");

        Aggregation aggregation = Aggregation.newAggregation(
                sortByDateTimeDesc,
                groupByCarId,
                replaceWithLatestLog,
                lookupTaskStatus,
                unwindTaskStatus,
                matchTaskStatusName,
                lookupCarInfo,
                unwindCarInfo,
                lookupCreatorUser,
                unwindCreatorUser,
                addCreatorUserFields,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                lookupAssignedUser,
                unwindAssignedUser,
                addAssignedUserFields,
                lookupAssignedUserRole,
                unwindAssignedUserRole,
                lookupAssignedUserPermission,
                unwindAssignedUserPermission,
                lookupProblemReport,
                unwindProblemReport,
                addProblemReportFields,
                lookupProblemReportCar,
                unwindProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getMappedResults();
    }

    public List<CarRepairLogResponseDTO> findLatestRepairLogForEachCar() {
        // Join carInfo
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");

        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        // مرتب‌سازی بر اساس جدیدترین زمان
        SortOperation sortByDateDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        // گرفتن جدیدترین لاگ از هر پلاک ماشین
        GroupOperation groupByLicensePlate = Aggregation.group("car.licensePlate")
                .first(Aggregation.ROOT).as("latestLog");

        // جایگزینی latestLog به root
        AggregationOperation replaceRoot = context -> new Document(
                "$replaceRoot", new Document("newRoot", "$latestLog")
        );

        // باقی عملیات قبلی
        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserId")
                .foreignField("_id")
                .as("creatorUser");
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        AggregationOperation addRoleIdObject = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId"))
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

        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject",
                        new Document("$cond",
                                new Document("if", new Document("$and", Arrays.asList(
                                        new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                        new Document("$ne", Arrays.asList("$assignedUserId", ""))
                                )))
                                        .append("then", new Document("$toObjectId", "$assignedUserId"))
                                        .append("else", null)
                        )
                )
        );

        LookupOperation lookupAssignedUser = LookupOperation.newLookup()
                .from("users")
                .localField("assignedUserIdObject")
                .foreignField("_id")
                .as("assignedUser");
        UnwindOperation unwindAssignedUser = Aggregation.unwind("assignedUser", true);

        AggregationOperation addAssignedUserRolePermissionIdObjects = context -> new Document("$addFields",
                new Document("assignedUser.roleIdObject", new Document("$toObjectId", "$assignedUser.roleId"))
                        .append("assignedUser.permissionIdObject", new Document("$toObjectId", "$assignedUser.permissionId"))
        );
        LookupOperation lookupAssignedUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("assignedUser.roleIdObject")
                .foreignField("_id")
                .as("assignedUser.role");
        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");
        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);
        UnwindOperation unwindAssignedUserPermission = Aggregation.unwind("assignedUser.permission", true);

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportId")
                .foreignField("_id")
                .as("problemReport");
        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);

        AggregationOperation addNestedProblemFields = context -> new Document("$addFields",
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

        AggregationOperation addProblemReportCreatorUserFields = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
                        .append("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId"))
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
        AggregationOperation convertProblemReportCreatorUserIdToString = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.userId", new Document("$toString", "$problemReport.creatorUser._id"))
        );

        ProjectionOperation project = Aggregation.project()
                .and("_id").as("id")
                .and("car").as("carInfo")
                .and("creatorUser._id").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("assignedUser._id").as("assignedUser.userId")
                .and("assignedUser.username").as("assignedUser.username")
                .and("assignedUser.firstName").as("assignedUser.firstName")
                .and("assignedUser.lastName").as("assignedUser.lastName")
                .and("assignedUser.role").as("assignedUser.role")
                .and("assignedUser.permission").as("assignedUser.permission")
                .and("description").as("description")
                .and("taskStatus").as("taskStatus")
                .and("dateTime").as("dateTime")
                .and("problemReport").as("problemReport");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                sortByDateDesc,
                groupByLicensePlate,
                replaceRoot,
                lookupCreatorUser,
                unwindCreatorUser,
                addRoleIdObject,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addAssignedUserIdObject,
                lookupAssignedUser,
                unwindAssignedUser,
                addAssignedUserRolePermissionIdObjects,
                lookupAssignedUserRole,
                unwindAssignedUserRole,
                lookupAssignedUserPermission,
                unwindAssignedUserPermission,
                lookupTaskStatus,
                unwindTaskStatus,
                lookupProblemReport,
                unwindProblemReport,
                addNestedProblemFields,
                lookupProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCar,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getMappedResults();
    }

    public List<CarRepairLogResponseDTO> findLatestRepairLogForEachCarAssignedToUser(String userId) {
        // 1. Join carInfo
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        // 2. مرتب‌سازی بر اساس جدیدترین زمان
        SortOperation sortByDateDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        // 3. گروه‌بندی روی پلاک ماشین برای گرفتن آخرین لاگ
        GroupOperation groupByLicensePlate = Aggregation.group("car.licensePlate")
                .first(Aggregation.ROOT).as("latestLog");

        // 4. انتقال latestLog به root
        AggregationOperation replaceRoot = context -> new Document("$replaceRoot", new Document("newRoot", "$latestLog"));

        // ✅ 5. فیلتر assignedUserId با userId ورودی
        MatchOperation matchAssignedUser = Aggregation.match(Criteria.where("assignedUserId").is(new ObjectId(userId)));

        // 6. باقی join و جوین‌های تو در تو (creatorUser، assignedUser، taskStatus، problemReport و ...)
        LookupOperation lookupCreatorUser = LookupOperation.newLookup()
                .from("users")
                .localField("creatorUserId")
                .foreignField("_id")
                .as("creatorUser");
        UnwindOperation unwindCreatorUser = Aggregation.unwind("creatorUser", true);
        AggregationOperation addRoleIdObject = context -> new Document("$addFields",
                new Document("creatorUser.roleIdObject", new Document("$toObjectId", "$creatorUser.roleId"))
                        .append("creatorUser.permissionIdObject", new Document("$toObjectId", "$creatorUser.permissionId")));
        LookupOperation lookupRole = LookupOperation.newLookup()
                .from("roles")
                .localField("creatorUser.roleIdObject")
                .foreignField("_id")
                .as("creatorUser.role");
        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject",
                        new Document("$cond",
                                new Document("if", new Document("$and", Arrays.asList(
                                        new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                        new Document("$ne", Arrays.asList("$assignedUserId", ""))
                                )))
                                        .append("then", new Document("$toObjectId", "$assignedUserId"))
                                        .append("else", null)
                        )
                )
        );
        LookupOperation lookupAssignedUser = LookupOperation.newLookup()
                .from("users")
                .localField("assignedUserIdObject")
                .foreignField("_id")
                .as("assignedUser");
        UnwindOperation unwindAssignedUser = Aggregation.unwind("assignedUser", true);
        AggregationOperation addAssignedUserRolePermissionIdObjects = context -> new Document("$addFields",
                new Document("assignedUser.roleIdObject", new Document("$toObjectId", "$assignedUser.roleId"))
                        .append("assignedUser.permissionIdObject", new Document("$toObjectId", "$assignedUser.permissionId")));
        LookupOperation lookupAssignedUserRole = LookupOperation.newLookup()
                .from("roles")
                .localField("assignedUser.roleIdObject")
                .foreignField("_id")
                .as("assignedUser.role");
        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");
        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);
        UnwindOperation unwindAssignedUserPermission = Aggregation.unwind("assignedUser.permission", true);

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        LookupOperation lookupProblemReport = LookupOperation.newLookup()
                .from("carProblemReport")
                .localField("problemReportId")
                .foreignField("_id")
                .as("problemReport");
        UnwindOperation unwindProblemReport = Aggregation.unwind("problemReport", true);
        AggregationOperation addNestedProblemFields = context -> new Document("$addFields",
                new Document("problemReport.carIdObject", new Document("$toObjectId", "$problemReport.carId"))
                        .append("problemReport.creatorUserIdObject", new Document("$toObjectId", "$problemReport.creatorUserId")));
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
        AggregationOperation addProblemReportCreatorUserFields = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.roleIdObject", new Document("$toObjectId", "$problemReport.creatorUser.roleId"))
                        .append("problemReport.creatorUser.permissionIdObject", new Document("$toObjectId", "$problemReport.creatorUser.permissionId")));
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
        AggregationOperation convertProblemReportCreatorUserIdToString = context -> new Document("$addFields",
                new Document("problemReport.creatorUser.userId", new Document("$toString", "$problemReport.creatorUser._id")));

        // 7. نهایی‌سازی projection
        ProjectionOperation project = Aggregation.project()
                .and("_id").as("id")
                .and("car").as("carInfo")
                .and("creatorUser._id").as("creatorUser.userId")
                .and("creatorUser.username").as("creatorUser.username")
                .and("creatorUser.firstName").as("creatorUser.firstName")
                .and("creatorUser.lastName").as("creatorUser.lastName")
                .and("creatorUser.role").as("creatorUser.role")
                .and("creatorUser.permission").as("creatorUser.permission")
                .and("assignedUser._id").as("assignedUser.userId")
                .and("assignedUser.username").as("assignedUser.username")
                .and("assignedUser.firstName").as("assignedUser.firstName")
                .and("assignedUser.lastName").as("assignedUser.lastName")
                .and("assignedUser.role").as("assignedUser.role")
                .and("assignedUser.permission").as("assignedUser.permission")
                .and("description").as("description")
                .and("taskStatus").as("taskStatus")
                .and("dateTime").as("dateTime")
                .and("problemReport").as("problemReport");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                sortByDateDesc,
                groupByLicensePlate,
                replaceRoot,
                matchAssignedUser,
                lookupCreatorUser,
                unwindCreatorUser,
                addRoleIdObject,
                lookupRole,
                unwindRole,
                lookupPermission,
                unwindPermission,
                addAssignedUserIdObject,
                lookupAssignedUser,
                unwindAssignedUser,
                addAssignedUserRolePermissionIdObjects,
                lookupAssignedUserRole,
                unwindAssignedUserRole,
                lookupAssignedUserPermission,
                unwindAssignedUserPermission,
                lookupTaskStatus,
                unwindTaskStatus,
                lookupProblemReport,
                unwindProblemReport,
                addNestedProblemFields,
                lookupProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCar,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getMappedResults();
    }



}
