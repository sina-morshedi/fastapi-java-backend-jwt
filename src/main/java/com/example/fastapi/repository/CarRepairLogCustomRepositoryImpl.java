package com.example.fastapi.repository;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import com.example.fastapi.dto.*;
import java.util.Date;



import com.example.fastapi.dto.CarRepairLogResponseDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



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
                        .append("assignedUserIdObject", new Document("$cond",
                                new Document("if", new Document("$and", Arrays.asList(
                                        new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                        new Document("$ne", Arrays.asList("$assignedUserId", ""))
                                )))
                                        .append("then", new Document("$toObjectId", "$assignedUserId"))
                                        .append("else", null)
                        ))
        );

        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

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
                .and("problemReport").as("problemReport")
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

        Aggregation aggregation = Aggregation.newAggregation(
                matchById,
                addFields,
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
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

        MatchOperation matchByLicensePlate = Aggregation.match(
                Criteria.where("car.licensePlate").is(licensePlate)
        );

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

        // assignedUser
        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                new Document("$ne", Arrays.asList("$assignedUserId", "")))))
                                .append("then", new Document("$toObjectId", "$assignedUserId"))
                                .append("else", null)
                ))
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

        // customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", "")))))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                )));
        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

        // taskStatus
        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        // problemReport
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
                .and("problemReport").as("problemReport")
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

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
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
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


    public List<CarRepairLogResponseDTO> findCarRepairLogsByLicensePlateAndTaskNames(
            String licensePlate, List<String> taskStatusNames) {

        // 1. Join carInfo
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        MatchOperation matchByLicensePlate = Aggregation.match(
                Criteria.where("car.licensePlate").is(licensePlate)
        );

        // 2. Join taskStatus and match by names
        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);
        MatchOperation matchByTaskStatusName = Aggregation.match(
                Criteria.where("taskStatus.taskStatusName").in(taskStatusNames)
        );

        // 3. Join creatorUser and their role & permission
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

        // 4. Add assignedUser and their role & permission
        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject",
                        new Document("$cond",
                                new Document("if", new Document("$and", Arrays.asList(
                                        new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                        new Document("$ne", Arrays.asList("$assignedUserId", "")))))
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

        // 5. Join customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", "")))))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                )));
        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

        // 6. Join problemReport + sub-joins
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

        // 7. Projection
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
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

        // 8. Build aggregation
        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                matchByLicensePlate,
                lookupTaskStatus,
                unwindTaskStatus,
                matchByTaskStatusName,
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
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
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


    public List<CarRepairLogResponseDTO> findCarRepairLogsByTaskNamesAndDateRange(
            List<String> taskStatusNames, Date startDate, Date endDate) {

        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");

        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");

        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        MatchOperation matchTaskAndDate = Aggregation.match(
                Criteria.where("taskStatus.taskStatusName").in(taskStatusNames)
                        .and("dateTime").gte(startDate).lte(endDate)
        );

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

        // ✳️ مراحل مربوط به Customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                )));

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");

        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

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
                .and("problemReport").as("problemReport")
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                lookupTaskStatus,
                unwindTaskStatus,
                matchTaskAndDate,
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

                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,

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


    public CarRepairLogResponseDTO findLatestCarRepairLogByLicensePlate(String licensePlate) {
        // Lookup اطلاعات مربوط به خودرو
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        // فیلتر بر اساس licensePlate
        MatchOperation matchByLicensePlate = Aggregation.match(Criteria.where("car.licensePlate").is(licensePlate));

        // مرتب سازی بر اساس تاریخ به صورت نزولی
        SortOperation sortByDateDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        // محدود کردن به یک رکورد
        LimitOperation limitToOne = Aggregation.limit(1);

        // افزودن customerIdObject به داده‌ها
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        ))).append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );

        // Lookup اطلاعات مشتری
        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

        // Lookup برای creatorUser
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

        // Lookup برای assignedUser
        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                new Document("$ne", Arrays.asList("$assignedUserId", ""))
                        ))).append("then", new Document("$toObjectId", "$assignedUserId"))
                                .append("else", null)
                ))
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

        // Lookup برای taskStatus
        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        // Lookup برای problemReport
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

        // Projection نهایی
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
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

        // تمام عملیات‌ها
        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                matchByLicensePlate,
                sortByDateDesc,
                limitToOne,
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
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

        return results.getUniqueMappedResult(); // فقط یک نتیجه می‌خواهیم
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
        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);

        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

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
        UnwindOperation unwindAssignedUserRole = Aggregation.unwind("assignedUser.role", true);

        LookupOperation lookupAssignedUserPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("assignedUser.permissionIdObject")
                .foreignField("_id")
                .as("assignedUser.permission");
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

        // 🔹 اضافه‌کردن customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

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
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

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
                // 🔹 اضافه‌شده‌ها برای customer:
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getMappedResults();
    }


    public List<CarRepairLogResponseDTO> findLatestCarRepairLogsByTaskStatusNamesAndAssignedUserId(
            List<String> taskStatusNames, String assignedUserId) {

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

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        MatchOperation matchAssignedUser = Aggregation.match(
                Criteria.where("assignedUserId").is(new ObjectId(assignedUserId)));

        SortOperation sortByDateTimeDesc = Aggregation.sort(Sort.Direction.DESC, "dateTime");
        GroupOperation groupByCarId = Aggregation.group("carId").first(Aggregation.ROOT).as("latestLog");
        ReplaceRootOperation replaceRootWithLatestLog = Aggregation.replaceRoot("latestLog");

        MatchOperation matchTaskStatus = Aggregation.match(
                Criteria.where("taskStatus.taskStatusName").in(taskStatusNames));

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

        // 🔹 اضافه‌کردن customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

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
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

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
                matchAssignedUser,
                sortByDateTimeDesc,
                groupByCarId,
                replaceRootWithLatestLog,
                matchTaskStatus,
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
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
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

        SortOperation sortByDateTimeDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));
        GroupOperation groupByCarId = Aggregation.group("carId")
                .first(Aggregation.ROOT).as("latestLog");
        ReplaceRootOperation replaceWithLatestLog = Aggregation.replaceRoot("latestLog");

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);
        MatchOperation matchTaskStatusName = Aggregation.match(
                Criteria.where("taskStatus.taskStatusName").is(taskStatusName)
        );

        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("carInfo");
        UnwindOperation unwindCarInfo = Aggregation.unwind("carInfo", true);

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

        // 🔹 اضافه‌کردن customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

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
                .and("problemReport").as("problemReport")
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

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
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
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

        // 5. Join creatorUser و نقش/دسترسی
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
        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");
        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        // 6. Join assignedUser و نقش/دسترسی
        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject",
                        new Document("$cond",
                                new Document("if", new Document("$and", Arrays.asList(
                                        new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                        new Document("$ne", Arrays.asList("$assignedUserId", ""))
                                )))
                                        .append("then", new Document("$toObjectId", "$assignedUserId"))
                                        .append("else", null))));
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

        // 7. Join taskStatus
        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        // 8. Join problemReport و تو در توهاش
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

        // 🔹 اضافه کردن customerIdObject و join کردن customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

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
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

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
                unwindProblemReportCar,
                lookupProblemReportCreatorUser,
                unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields,
                lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission,
                unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission,
                convertProblemReportCreatorUserIdToString,
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        return results.getMappedResults();
    }

    public List<CarRepairLogResponseDTO> findLatestRepairLogForEachCarFiltered(
            String customerFullName,
            String taskStatusName
    ) {
        // 1. Join carInfo
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        // 2. Sort by date descending
        SortOperation sortByDateDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        // 3. Group by licensePlate
        GroupOperation groupByLicensePlate = Aggregation.group("car.licensePlate")
                .first(Aggregation.ROOT).as("latestLog");

        // 4. Replace root
        AggregationOperation replaceRoot = context -> new Document("$replaceRoot", new Document("newRoot", "$latestLog"));

        // 5. CreatorUser join and roles/permissions
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
        LookupOperation lookupPermission = LookupOperation.newLookup()
                .from("permissions")
                .localField("creatorUser.permissionIdObject")
                .foreignField("_id")
                .as("creatorUser.permission");
        UnwindOperation unwindRole = Aggregation.unwind("creatorUser.role", true);
        UnwindOperation unwindPermission = Aggregation.unwind("creatorUser.permission", true);

        // 6. AssignedUser join
        AggregationOperation addAssignedUserIdObject = context -> new Document("$addFields",
                new Document("assignedUserIdObject",
                        new Document("$cond",
                                new Document("if", new Document("$and", Arrays.asList(
                                        new Document("$ne", Arrays.asList("$assignedUserId", null)),
                                        new Document("$ne", Arrays.asList("$assignedUserId", ""))
                                )))
                                        .append("then", new Document("$toObjectId", "$assignedUserId"))
                                        .append("else", null))));
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

        // 7. TaskStatus join
        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("taskStatusId")
                .foreignField("_id")
                .as("taskStatus");
        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        // 8. ProblemReport join
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

        // 9. Customer join
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );
        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

        // 10. Projection
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
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo, unwindCar, sortByDateDesc, groupByLicensePlate, replaceRoot,
                lookupCreatorUser, unwindCreatorUser, addRoleIdObject,
                lookupRole, unwindRole, lookupPermission, unwindPermission,
                addAssignedUserIdObject, lookupAssignedUser, unwindAssignedUser,
                addAssignedUserRolePermissionIdObjects, lookupAssignedUserRole,
                unwindAssignedUserRole, lookupAssignedUserPermission, unwindAssignedUserPermission,
                lookupTaskStatus, unwindTaskStatus,
                lookupProblemReport, unwindProblemReport, addNestedProblemFields,
                lookupProblemReportCar, unwindProblemReportCar,
                lookupProblemReportCreatorUser, unwindProblemReportCreatorUser,
                addProblemReportCreatorUserFields, lookupProblemReportCreatorUserRole,
                lookupProblemReportCreatorUserPermission, unwindProblemReportCreatorUserRole,
                unwindProblemReportCreatorUserPermission, convertProblemReportCreatorUserIdToString,
                addCustomerIdObject, lookupCustomer, unwindCustomer,
                project
        );

        AggregationResults<CarRepairLogResponseDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                CarRepairLogResponseDTO.class
        );

        List<CarRepairLogResponseDTO> allLogs = results.getMappedResults();

        // فیلتر نهایی بر اساس fullName و taskStatusName
        return allLogs.stream()
                .filter(log -> {
                    boolean customerMatch = true;
                    boolean statusMatch = true;

                    if (customerFullName != null && !customerFullName.trim().isEmpty()) {
                        customerMatch = log.getCustomer() != null
                                && log.getCustomer().getFullName() != null
                                && log.getCustomer().getFullName().toLowerCase().contains(customerFullName.toLowerCase());
                    }

                    if (taskStatusName != null && !taskStatusName.trim().isEmpty()) {
                        statusMatch = log.getTaskStatus() != null
                                && log.getTaskStatus().getTaskStatusName() != null
                                && log.getTaskStatus().getTaskStatusName().equalsIgnoreCase(taskStatusName);
                    }

                    return customerMatch && statusMatch;
                })
                .collect(Collectors.toList());
    }




    public List<TaskStatusCountDTO> countCarsByLatestTaskStatus() {
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        SortOperation sortByDateDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        GroupOperation groupByLicensePlate = Aggregation.group("car.licensePlate")
                .first(Aggregation.ROOT).as("latestLog");

        AggregationOperation replaceRoot = context -> new Document("$replaceRoot", new Document("newRoot", "$latestLog"));

        GroupOperation groupByTaskStatus = Aggregation.group("taskStatusId")
                .count().as("count");

        LookupOperation lookupTaskStatus = LookupOperation.newLookup()
                .from("taskStatus")
                .localField("_id")  // در گروه‌بندی، _id همان taskStatusId است
                .foreignField("_id")
                .as("taskStatus");

        UnwindOperation unwindTaskStatus = Aggregation.unwind("taskStatus", true);

        ProjectionOperation project = Aggregation.project()
                .and("_id").as("taskStatusId")
                .and("taskStatus.taskStatusName").as("taskStatusName")
                .and("count").as("count");

        Aggregation aggregation = Aggregation.newAggregation(
                lookupCarInfo,
                unwindCar,
                sortByDateDesc,
                groupByLicensePlate,
                replaceRoot,
                groupByTaskStatus,
                lookupTaskStatus,
                unwindTaskStatus,
                project
        );

        AggregationResults<TaskStatusCountDTO> results = mongoTemplate.aggregate(
                aggregation,
                "carRepairLog",
                TaskStatusCountDTO.class
        );

        return results.getMappedResults();
    }

    public List<CarRepairLogResponseDTO> findLatestRepairLogForEachCarAssignedToUser(String userId) {
        LookupOperation lookupCarInfo = LookupOperation.newLookup()
                .from("carInfo")
                .localField("carId")
                .foreignField("_id")
                .as("car");
        UnwindOperation unwindCar = Aggregation.unwind("car", true);

        SortOperation sortByDateDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "dateTime"));

        GroupOperation groupByLicensePlate = Aggregation.group("car.licensePlate")
                .first(Aggregation.ROOT).as("latestLog");

        AggregationOperation replaceRoot = context -> new Document("$replaceRoot", new Document("newRoot", "$latestLog"));

        MatchOperation matchAssignedUser = Aggregation.match(Criteria.where("assignedUserId").is(new ObjectId(userId)));

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
                                        .append("else", null))));
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

        // 🔹 اضافه کردن customerIdObject و join کردن customer
        AggregationOperation addCustomerIdObject = context -> new Document("$addFields",
                new Document("customerIdObject", new Document("$cond",
                        new Document("if", new Document("$and", Arrays.asList(
                                new Document("$ne", Arrays.asList("$customerId", null)),
                                new Document("$ne", Arrays.asList("$customerId", ""))
                        )))
                                .append("then", new Document("$toObjectId", "$customerId"))
                                .append("else", null)
                ))
        );

        LookupOperation lookupCustomer = LookupOperation.newLookup()
                .from("customers")
                .localField("customerIdObject")
                .foreignField("_id")
                .as("customer");
        UnwindOperation unwindCustomer = Aggregation.unwind("customer", true);

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
                .and("partsUsed").as("partsUsed")
                .and("paymentRecords").as("paymentRecords")
                .and("customer").as("customer");

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
                addCustomerIdObject,
                lookupCustomer,
                unwindCustomer,
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